package com.openplatform.open.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.openplatform.common.base.model.resp.BaseDetailResp;
import com.openplatform.open.enums.SubscribeStatusEnum;

import java.io.Serial;

@Data
@Schema(description = "订阅响应参数")
public class SubscribeResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "应用ID", example = "1")
    private Long appId;

    @Schema(description = "API ID", example = "1")
    private Long apiId;

    @Schema(description = "状态", example = "0")
    private SubscribeStatusEnum status;

    @Schema(description = "配额限制", example = "1000")
    private Integer quotaLimit;
}