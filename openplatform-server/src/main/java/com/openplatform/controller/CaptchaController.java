package com.openplatform.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.continew.starter.web.model.R;

@Tag(name = "验证码管理")
@SaIgnore
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Operation(summary = "获取行为验证码")
    @GetMapping("/behavior")
    public R<Void> getBehaviorCaptcha() {
        return R.ok();
    }

    @Operation(summary = "校验行为验证码")
    @PostMapping("/behavior")
    public R<Void> checkBehaviorCaptcha() {
        return R.ok();
    }
}