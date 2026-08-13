package com.openplatform.open.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "调用日志查询条件")
public class ApiLogQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "应用ID", example = "1")
    private Long appId;

    @Schema(description = "API ID", example = "1")
    private Long apiId;

    @Schema(description = "状态码", example = "200")
    private Integer statusCode;

    @Schema(description = "开始时间", example = "2024-01-01 00:00:00")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", example = "2024-12-31 23:59:59")
    private LocalDateTime endTime;
}