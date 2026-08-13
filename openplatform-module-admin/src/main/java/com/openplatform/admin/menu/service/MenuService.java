package com.openplatform.admin.menu.service;

import com.openplatform.admin.menu.model.entity.MenuDO;
import com.openplatform.common.base.service.BaseService;

import java.util.List;
import java.util.Set;

public interface MenuService extends BaseService<MenuDO, MenuDO, MenuDO, MenuDO> {

    List<MenuDO> listByUserId(Long userId);

    List<MenuDO> listByRoleId(Long roleId);

    Set<String> listPermissionByUserId(Long userId);

    List<MenuDO> list();
}