package com.openplatform.open.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ApiGatewayService {

    void handle(HttpServletRequest request, HttpServletResponse response);
}