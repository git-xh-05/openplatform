package com.openplatform.open.sign;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.sign.template.SaSignTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import com.openplatform.common.enums.DisEnableStatusEnum;
import com.openplatform.open.model.entity.OpenAppDO;
import com.openplatform.open.service.OpenAppService;
import top.continew.starter.core.util.validation.ValidationUtils;

import java.util.Map;

@Primary
@Component
@RequiredArgsConstructor
public class OpenPlatformSignTemplate extends SaSignTemplate {

    private final OpenAppService openAppService;
    public static final String ACCESS_KEY = "accessKey";

    @Override
    public void checkParamMap(Map<String, String> paramMap) {
        String timestampValue = paramMap.get(timestamp);
        String nonceValue = paramMap.get(nonce);
        String signValue = paramMap.get(sign);
        String accessKeyValue = paramMap.get(ACCESS_KEY);

        ValidationUtils.throwIfBlank(timestampValue, "timestamp不能为空");
        ValidationUtils.throwIfBlank(nonceValue, "nonce不能为空");
        ValidationUtils.throwIfBlank(signValue, "sign不能为空");
        ValidationUtils.throwIfBlank(accessKeyValue, "accessKey不能为空");
        OpenAppDO app = openAppService.getByAccessKey(accessKeyValue);
        ValidationUtils.throwIfNull(app, "accessKey无效");
        ValidationUtils.throwIfEqual(DisEnableStatusEnum.DISABLE, app.getStatus(), "应用已被禁用");
        ValidationUtils.throwIf(app.isExpired(), "应用已过期");

        super.checkTimestamp(Long.parseLong(timestampValue));
        super.checkNonce(nonceValue);
        paramMap.put(key, app.getSecretKey());
        super.checkSign(paramMap, signValue);
    }

    @Override
    public String createSign(Map<String, ?> paramMap) {
        ValidationUtils.throwIfEmpty(paramMap.get(key), "秘钥缺失");
        paramMap.remove(sign);
        return SaSecureUtil.md5(super.joinParamsDictSort(paramMap));
    }
}