package com.openplatform.admin.menu.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.openplatform.common.base.model.entity.BaseDO;
import com.openplatform.common.enums.DisEnableStatusEnum;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class MenuDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private Long parentId;
    private Integer type;
    private String path;
    private String name;
    private String component;
    private String redirect;
    private String icon;
    private Boolean isExternal;
    private Boolean isCache;
    private Boolean isHidden;
    private String permission;
    private Integer sort;
    private DisEnableStatusEnum status;
}