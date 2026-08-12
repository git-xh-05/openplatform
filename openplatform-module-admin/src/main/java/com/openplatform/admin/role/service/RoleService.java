package com.openplatform.admin.role.service;

import com.openplatform.admin.role.model.entity.RoleDO;
import com.openplatform.common.base.service.BaseService;

import java.util.List;
import java.util.Set;

public interface RoleService extends BaseService<RoleDO, RoleDO, RoleDO, RoleDO> {

    List<RoleDO> listByUserId(Long userId);

    Set<String> listCodeByUserId(Long userId);
}