package com.openplatform.open.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.openplatform.common.base.model.entity.BaseDO;
import com.openplatform.open.enums.SubscribeStatusEnum;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@TableName("open_subscribe")
public class SubscribeDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long appId;
    private Long apiId;
    private SubscribeStatusEnum status = SubscribeStatusEnum.PENDING;
    private Integer quotaLimit;
    private Long approveUser;
    private LocalDateTime approveTime;
}