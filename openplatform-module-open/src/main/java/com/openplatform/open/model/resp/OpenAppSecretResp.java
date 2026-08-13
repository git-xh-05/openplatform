package com.openplatform.open.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "应用密钥响应参数")
public class OpenAppSecretResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Access Key", example = "YjUyMGJjYjIxNTE0NDAxMWE1NmRiY2")
    private String accessKey;

    @Schema(description = "Secret Key", example = "MDI2YzQ3YTU1NGEyNDM1ZWIwNTU5NmNjNmZjM2M2Nzg=")
    private String secretKey;
}