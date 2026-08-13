package com.openplatform.admin.role.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.openplatform.common.base.model.entity.BaseDO;
import com.openplatform.common.enums.DisEnableStatusEnum;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class RoleDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String code;
    private String description;
    private Integer sort;
    private Boolean isSystem;
    private DisEnableStatusEnum status;
}