package com.openplatform.open.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.openplatform.open.sign.OpenPlatformSignTemplate;

import java.io.IOException;

@Component
@Order(-100)
@RequiredArgsConstructor
public class OpenApiSignFilter implements Filter {

    private final OpenPlatformSignTemplate signTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();

        if (!path.startsWith("/open-api/")) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        SaRequest saRequest = SaHolder.getRequest();
        signTemplate.checkRequest(saRequest);

        chain.doFilter(servletRequest, servletResponse);
    }
}