package com.openplatform.open.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.openplatform.common.enums.DisEnableStatusEnum;
import com.openplatform.open.mapper.ApiLogMapper;
import com.openplatform.open.mapper.ApiMapper;
import com.openplatform.open.mapper.SubscribeMapper;
import com.openplatform.open.model.entity.ApiDO;
import com.openplatform.open.model.entity.ApiLogDO;
import com.openplatform.open.model.entity.OpenAppDO;
import com.openplatform.open.model.entity.SubscribeDO;
import com.openplatform.open.service.ApiGatewayService;
import com.openplatform.open.service.OpenAppService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApiGatewayServiceImpl implements ApiGatewayService {

    private final ApiMapper apiMapper;
    private final SubscribeMapper subscribeMapper;
    private final OpenAppService openAppService;
    private final RedissonClient redissonClient;
    private final RestTemplate restTemplate;
    private final ApiLogMapper apiLogMapper;

    private static final List<String> AUTH_PARAMS = List.of("accessKey", "sign", "timestamp", "nonce");

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        ApiLogDO log = new ApiLogDO();
        log.setClientIp(JakartaServletUtil.getClientIP(request));
        log.setCreateTime(LocalDateTime.now());

        try {
            String path = request.getRequestURI().replaceFirst("^/open-api", "");
            String method = request.getMethod();

            ApiDO api = apiMapper.selectByPathAndMethod(path, method);
            if (api == null) {
                writeResponse(response, HttpStatus.NOT_FOUND.value(), "API not found");
                return;
            }
            log.setApiId(api.getId());

            String accessKey = request.getParameter("accessKey");
            OpenAppDO app = openAppService.getByAccessKey(accessKey);
            if (app == null || DisEnableStatusEnum.DISABLE.equals(app.getStatus()) || app.isExpired()) {
                writeResponse(response, HttpStatus.FORBIDDEN.value(), "App is invalid");
                return;
            }
            log.setAppId(app.getId());

            String blackList = app.getIpBlacklist();
            String ip = log.getClientIp();
            if(blackList != null){
                String[] black = blackList.split(",");
                if (Arrays.asList(black).contains(ip)){
                    writeResponse(response, HttpStatus.FORBIDDEN.value(),"IP is blacklisted");
                    return;
                }
            }

            SubscribeDO subscribe = subscribeMapper.selectByAppIdAndApiId(app.getId(), api.getId());
            if (subscribe == null) {
                writeResponse(response, HttpStatus.FORBIDDEN.value(), "No subscription");
                return;
            }

            String today = LocalDateTime.now(ZoneId.of("Asia/Shanghai"))
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String quotaKey = "quota:" + app.getId() + ":" + api.getId() + ":" + today;
            Integer quotaLimit = subscribe.getQuotaLimit();
            RAtomicLong rAtomicLong = redissonClient.getAtomicLong(quotaKey);
            rAtomicLong.expire(Duration.ofDays(1));
            if (quotaLimit != null){
                long count = rAtomicLong.incrementAndGet();
                if (count > quotaLimit){
                    writeResponse(response,HttpStatus.FORBIDDEN.value(), "request is limited");
                    return;
                }
            }

            String rateKey = "rate:" + app.getId() + ":" + api.getId();
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateKey);
            rateLimiter.trySetRate(org.redisson.api.RateType.OVERALL, Optional.ofNullable(app.getRateLimit()).orElse(100), Duration.ofSeconds(1));
            if (!rateLimiter.tryAcquire()) {
                writeResponse(response, HttpStatus.TOO_MANY_REQUESTS.value(), "Rate limit exceeded");
                return;
            }

            String queryString = request.getQueryString();
            String cleanQuery = "";
            if (StrUtil.isNotBlank(queryString)) {
                List<String> keptParams = new ArrayList<>();
                for (String param : queryString.split("&")) {
                    String key = param.split("=")[0];
                    if (!AUTH_PARAMS.contains(key)) {
                        keptParams.add(param);
                    }
                }
                cleanQuery = String.join("&", keptParams);
            }

            String targetUrl = api.getServiceUrl();
            if (StrUtil.isNotBlank(cleanQuery)) {
                targetUrl += "?" + cleanQuery;
            }

            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (!"host".equalsIgnoreCase(headerName) && !"content-length".equalsIgnoreCase(headerName)) {
                    headers.set(headerName, request.getHeader(headerName));
                }
            }

            HttpEntity<String> requestEntity;
            String contentType = request.getContentType();
            if (contentType != null && !"GET".equalsIgnoreCase(method) && !"DELETE".equalsIgnoreCase(method)) {
                String body = JakartaServletUtil.getBody(request);
                log.setRequestParams(body);
                requestEntity = new HttpEntity<>(body, headers);
            } else {
                log.setRequestParams(queryString);
                requestEntity = new HttpEntity<>(headers);
            }

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                targetUrl,
                HttpMethod.valueOf(method.toUpperCase()),
                requestEntity,
                String.class
            );

            log.setStatusCode(responseEntity.getStatusCode().value());
            log.setResponseBody(responseEntity.getBody());
            log.setCostTime(System.currentTimeMillis() - startTime);

            response.setStatus(responseEntity.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(responseEntity.getBody());

        } catch (Exception e) {
            log.setStatusCode(500);
            log.setErrorMessage(e.getMessage());
            log.setCostTime(System.currentTimeMillis() - startTime);
            writeResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        } finally {
            try {
                apiLogMapper.insert(log);
            } catch (Exception ignored) {
            }
        }
    }

    private void writeResponse(HttpServletResponse response, int status, String msg) {
        try {
            response.setStatus(status);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"code\":" + status + ",\"msg\":\"" + msg + "\"}");
        } catch (Exception ignored) {
        }
    }
}