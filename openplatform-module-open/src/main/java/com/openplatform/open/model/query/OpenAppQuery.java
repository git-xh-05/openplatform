package com.openplatform.open.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.starter.data.annotation.Query;
import top.continew.starter.data.enums.QueryType;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "应用查询条件")
public class OpenAppQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "关键词", example = "应用1")
    @Query(columns = {"name", "description"}, type = QueryType.LIKE)
    private String description;
}