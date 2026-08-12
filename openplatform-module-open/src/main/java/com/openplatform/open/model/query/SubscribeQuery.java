package com.openplatform.open.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.starter.data.annotation.Query;
import top.continew.starter.data.enums.QueryType;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "订阅查询条件")
public class SubscribeQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "应用ID", example = "1")
    private Long appId;

    @Schema(description = "API ID", example = "1")
    private Long apiId;

    @Schema(description = "状态", example = "0")
    private String status;

    @Schema(description = "关键词", example = "应用名称")
    @Query(columns = {"appId", "apiId"}, type = QueryType.EQ)
    private String description;
}