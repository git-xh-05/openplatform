package com.openplatform.open.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.openplatform.common.base.model.entity.BaseDO;
import com.openplatform.common.enums.DisEnableStatusEnum;
import top.continew.starter.encrypt.field.annotation.FieldEncrypt;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@TableName("open_platform_app")
public class OpenAppDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    @FieldEncrypt
    private String accessKey;

    @FieldEncrypt
    private String secretKey;

    private LocalDateTime expireTime;

    private String ipBlacklist;

    private Integer rateLimit;

    private String description;

    private DisEnableStatusEnum status;

    public boolean isExpired() {
        if (expireTime == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expireTime);
    }
}