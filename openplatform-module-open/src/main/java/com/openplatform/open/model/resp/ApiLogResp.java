package com.openplatform.open.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "调用日志响应参数")
public class ApiLogResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "应用ID", example = "1")
    private Long appId;

    @Schema(description = "API ID", example = "1")
    private Long apiId;

    @Schema(description = "状态码", example = "200")
    private Integer statusCode;

    @Schema(description = "耗时（毫秒）", example = "150")
    private Long costTime;

    @Schema(description = "客户端 IP", example = "127.0.0.1")
    private String clientIp;

    @Schema(description = "调用时间", example = "2024-01-01 12:00:00")
    private LocalDateTime createTime;
}