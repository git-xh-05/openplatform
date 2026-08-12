package com.openplatform.open.model.resp;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.openplatform.common.base.model.resp.BaseDetailResp;
import com.openplatform.common.enums.DisEnableStatusEnum;
import top.continew.starter.excel.converter.ExcelBaseEnumConverter;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
@Schema(description = "应用详情响应参数")
public class OpenAppDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称", example = "应用1")
    @ExcelProperty(value = "名称", order = 2)
    private String name;

    @Schema(description = "Access Key", example = "YjUyMGJjYjIxNTE0NDAxMWE1NmRiY2")
    @ExcelProperty(value = "Access Key", order = 3)
    private String accessKey;

    @Schema(description = "失效时间", example = "2023-08-08 08:08:08", type = "string")
    @ExcelProperty(value = "失效时间", order = 4)
    private LocalDateTime expireTime;

    @Schema(description = "IP 黑名单", example = "127.0.0.1,192.168.1.1")
    @ExcelProperty(value = "IP 黑名单", order = 5)
    private String ipBlacklist;

    @Schema(description = "速率限制", example = "100")
    @ExcelProperty(value = "速率限制", order = 6)
    private Integer rateLimit;

    @Schema(description = "状态", example = "1")
    @ExcelProperty(value = "状态", converter = ExcelBaseEnumConverter.class, order = 7)
    private DisEnableStatusEnum status;

    @Schema(description = "描述", example = "应用1描述信息")
    @ExcelProperty(value = "描述", order = 8)
    private String description;
}