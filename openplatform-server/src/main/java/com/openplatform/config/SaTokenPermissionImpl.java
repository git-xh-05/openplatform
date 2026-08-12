package com.openplatform.config;

import cn.dev33.satoken.stp.StpInterface;
import com.openplatform.admin.role.service.RoleService;
import com.openplatform.admin.menu.service.MenuService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaTokenPermissionImpl implements StpInterface {

    private final RoleService roleService;
    private final MenuService menuService;

    public SaTokenPermissionImpl(RoleService roleService, MenuService menuService) {
        this.roleService = roleService;
        this.menuService = menuService;
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>(menuService.listPermissionByUserId(Long.parseLong(loginId.toString())));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return new ArrayList<>(roleService.listCodeByUserId(Long.parseLong(loginId.toString())));
    }
}