package com.openplatform.admin.auth.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "登录请求参数")
public class LoginReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名", example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "RSA加密后的密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "验证码二次校验参数")
    private String captchaVerification;
}