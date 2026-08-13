package com.openplatform.admin.auth.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.openplatform.admin.auth.model.req.LoginReq;
import com.openplatform.admin.auth.model.resp.LoginResp;
import com.openplatform.admin.auth.model.resp.RouteResp;
import com.openplatform.admin.auth.model.resp.UserInfoResp;
import com.openplatform.admin.auth.service.AuthService;
import top.continew.starter.web.model.R;

import java.util.List;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @SaIgnore
    @Operation(summary = "登录")
    @PostMapping("/login")
    public R<LoginResp> login(@Valid @RequestBody LoginReq req) {
        return R.ok(authService.login(req));
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public R<Void> logout() {
        authService.logout();
        return R.ok();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/user/info")
    public R<UserInfoResp> userInfo() {
        return R.ok(authService.getUserInfo());
    }

    @Operation(summary = "获取路由信息")
    @GetMapping("/user/route")
    public R<List<RouteResp>> route() {
        return R.ok(authService.buildRouteTree());
    }
}