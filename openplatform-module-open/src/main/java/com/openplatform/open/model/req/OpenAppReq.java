package com.openplatform.open.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import com.openplatform.common.enums.DisEnableStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "应用创建或修改请求参数")
public class OpenAppReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称", example = "应用1")
    @NotBlank(message = "名称不能为空")
    @Length(max = 100, message = "名称长度不能超过 {max} 个字符")
    private String name;

    @Schema(description = "失效时间", example = "2023-08-08 23:59:59", type = "string")
    @Future(message = "失效时间必须是未来时间")
    private LocalDateTime expireTime;

    @Schema(description = "IP 黑名单", example = "127.0.0.1,192.168.1.1")
    private String ipBlacklist;

    @Schema(description = "速率限制（每秒请求数）", example = "100")
    private Integer rateLimit;

    @Schema(description = "描述", example = "应用1描述信息")
    @Length(max = 200, message = "描述长度不能超过 {max} 个字符")
    private String description;

    @Schema(description = "状态", example = "1")
    private DisEnableStatusEnum status;

    @Schema(hidden = true)
    private String accessKey;

    @Schema(hidden = true)
    private String secretKey;
}