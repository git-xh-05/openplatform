package com.openplatform.open.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "订阅创建或修改请求参数")
public class SubscribeReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "应用ID", example = "1")
    @NotNull(message = "应用不能为空")
    private Long appId;

    @Schema(description = "API ID", example = "1")
    @NotNull(message = "API不能为空")
    private Long apiId;

    @Schema(description = "配额限制", example = "1000")
    private Integer quotaLimit;
}