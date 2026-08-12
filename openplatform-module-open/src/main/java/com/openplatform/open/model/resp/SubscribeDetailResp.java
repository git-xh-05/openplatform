package com.openplatform.open.model.resp;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.openplatform.common.base.model.resp.BaseDetailResp;
import com.openplatform.open.enums.SubscribeStatusEnum;
import top.continew.starter.excel.converter.ExcelBaseEnumConverter;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
@Schema(description = "订阅详情响应参数")
public class SubscribeDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "应用ID", example = "1")
    @ExcelProperty(value = "应用ID", order = 2)
    private Long appId;

    @Schema(description = "API ID", example = "1")
    @ExcelProperty(value = "API ID", order = 3)
    private Long apiId;

    @Schema(description = "状态", example = "0")
    @ExcelProperty(value = "状态", converter = ExcelBaseEnumConverter.class, order = 4)
    private SubscribeStatusEnum status;

    @Schema(description = "配额限制", example = "1000")
    @ExcelProperty(value = "配额限制", order = 5)
    private Integer quotaLimit;

    @Schema(description = "审批人", example = "1")
    private Long approveUser;

    @Schema(description = "审批时间", example = "2023-08-08 08:08:08", type = "string")
    private LocalDateTime approveTime;
}