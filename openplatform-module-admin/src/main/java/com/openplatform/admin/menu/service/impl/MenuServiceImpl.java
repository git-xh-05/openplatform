package com.openplatform.admin.menu.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import com.openplatform.admin.menu.mapper.MenuMapper;
import com.openplatform.admin.menu.model.entity.MenuDO;
import com.openplatform.admin.menu.service.MenuService;
import com.openplatform.common.base.service.BaseServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, MenuDO, MenuDO, MenuDO, MenuDO, MenuDO> implements MenuService {

    @Override
    public List<MenuDO> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public List<MenuDO> listByRoleId(Long roleId) {
        return baseMapper.selectByRoleId(roleId);
    }

    @Override
    public Set<String> listPermissionByUserId(Long userId) {
        List<String> permissions = baseMapper.selectPermissionByUserId(userId);
        if (CollUtil.isEmpty(permissions)) {
            return Set.of();
        }
        return new HashSet<>(permissions);
    }

    @Override
    public List<MenuDO> list() {
        return baseMapper.selectList(null);
    }
}