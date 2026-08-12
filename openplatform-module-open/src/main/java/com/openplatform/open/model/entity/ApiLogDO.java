package com.openplatform.open.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.openplatform.common.base.model.entity.BaseDO;

import java.io.Serial;

@Data
@TableName("open_api_log")
public class ApiLogDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long appId;
    private Long apiId;
    private String requestParams;
    private String responseBody;
    private Integer statusCode;
    private Long costTime;
    private String clientIp;
    private String errorMessage;
}