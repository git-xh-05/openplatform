package com.openplatform.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.openplatform.common.base.model.entity.BaseDO;

import java.io.Serial;

@Data
@TableName("sys_role_menu")
public class RoleMenuDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private Long menuId;
}