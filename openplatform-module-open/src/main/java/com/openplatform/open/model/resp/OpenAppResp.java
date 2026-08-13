package com.openplatform.open.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.openplatform.common.base.model.resp.BaseDetailResp;
import com.openplatform.common.enums.DisEnableStatusEnum;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@Schema(description = "应用响应参数")
public class OpenAppResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称", example = "应用1")
    private String name;

    @Schema(description = "Access Key", example = "YjUyMGJjYjIxNTE0NDAxMWE1NmRiY2")
    private String accessKey;

    @Schema(description = "失效时间", example = "2023-08-08 08:08:08", type = "string")
    private LocalDateTime expireTime;

    @Schema(description = "IP 黑名单", example = "127.0.0.1,192.168.1.1")
    private String ipBlacklist;

    @Schema(description = "速率限制", example = "100")
    private Integer rateLimit;

    @Schema(description = "状态", example = "1")
    private DisEnableStatusEnum status;

    @Schema(description = "描述", example = "应用1描述信息")
    private String description;
}