package com.openplatform.open.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openplatform.open.service.ApiGatewayService;

@Tag(name = "开放 API")
@RestController
@RequiredArgsConstructor
public class OpenApiGatewayController {

    private final ApiGatewayService apiGatewayService;

    @RequestMapping("/open-api/**")
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        apiGatewayService.handle(request, response);
    }
}