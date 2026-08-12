package com.openplatform.admin.auth.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "登录响应参数")
public class LoginResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Token")
    private String token;

    @Schema(description = "租户ID")
    private Long tenantId;
}