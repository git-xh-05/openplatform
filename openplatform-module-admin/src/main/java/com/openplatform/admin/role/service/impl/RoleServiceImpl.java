package com.openplatform.admin.role.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import com.openplatform.admin.role.mapper.RoleMapper;
import com.openplatform.admin.role.model.entity.RoleDO;
import com.openplatform.admin.role.service.RoleService;
import com.openplatform.common.base.service.BaseServiceImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, RoleDO, RoleDO, RoleDO, RoleDO, RoleDO> implements RoleService {

    @Override
    public List<RoleDO> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public Set<String> listCodeByUserId(Long userId) {
        List<RoleDO> roles = this.listByUserId(userId);
        if (CollUtil.isEmpty(roles)) {
            return Set.of();
        }
        return roles.stream().map(RoleDO::getCode).collect(Collectors.toSet());
    }
}