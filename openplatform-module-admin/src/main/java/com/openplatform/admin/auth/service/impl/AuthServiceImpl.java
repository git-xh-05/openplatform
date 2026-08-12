package com.openplatform.admin.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openplatform.admin.auth.model.req.LoginReq;
import com.openplatform.admin.auth.model.resp.LoginResp;
import com.openplatform.admin.auth.model.resp.RouteResp;
import com.openplatform.admin.auth.model.resp.UserInfoResp;
import com.openplatform.admin.auth.service.AuthService;
import com.openplatform.admin.menu.model.entity.MenuDO;
import com.openplatform.admin.menu.service.MenuService;
import com.openplatform.admin.role.service.RoleService;
import com.openplatform.admin.user.model.entity.UserDO;
import com.openplatform.admin.user.service.UserService;
import com.openplatform.common.context.UserContext;
import com.openplatform.common.context.UserContextHolder;
import com.openplatform.common.enums.DisEnableStatusEnum;
import com.openplatform.common.util.SecureUtils;
import com.openplatform.common.constant.GlobalConstants;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final MenuService menuService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserService userService, RoleService roleService, MenuService menuService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.menuService = menuService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResp login(LoginReq req) {
        UserDO user = userService.getByUsername(req.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (DisEnableStatusEnum.DISABLE.equals(user.getStatus())) {
            throw new RuntimeException("用户已被禁用");
        }
        String decryptedPassword = SecureUtils.decryptPasswordByRsaPrivateKey(req.getPassword(), "密码解密失败");
        if (!passwordEncoder.matches(decryptedPassword, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        StpUtil.login(user.getId());
        Set<String> roleCodes = roleService.listCodeByUserId(user.getId());
        Set<String> permissions = menuService.listPermissionByUserId(user.getId());
        UserContext context = new UserContext();
        context.setId(user.getId());
        context.setUsername(user.getUsername());
        context.setRoleCodes(roleCodes);
        context.setPermissions(permissions);
        UserContextHolder.setContext(context);
        LoginResp resp = new LoginResp();
        resp.setToken(StpUtil.getTokenValue());
        return resp;
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public UserInfoResp getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        UserDO user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        Set<String> permissions = menuService.listPermissionByUserId(userId);
        Set<String> roleCodes = roleService.listCodeByUserId(userId);
        UserInfoResp resp = new UserInfoResp();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setNickname(user.getNickname());
        resp.setGender(user.getGender());
        resp.setEmail(user.getEmail());
        resp.setPhone(user.getPhone());
        resp.setAvatar(user.getAvatar());
        resp.setDescription(user.getDescription());
        resp.setPermissions(permissions);
        resp.setRoles(roleCodes);
        if (user.getPwdResetTime() != null) {
            resp.setPwdResetTime(user.getPwdResetTime().toString());
        }
        return resp;
    }

    @Override
    public List<RouteResp> buildRouteTree() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<MenuDO> menus;
        if (StpUtil.hasRole("super_admin")) {
            menus = menuService.list();
        } else {
            menus = menuService.listByUserId(userId);
        }
        if (CollUtil.isEmpty(menus)) {
            return List.of();
        }
        List<MenuDO> filtered = menus.stream()
            .filter(m -> m.getType() != 3)
            .collect(Collectors.toList());
        TreeNodeConfig config = new TreeNodeConfig();
        config.setIdKey("id");
        config.setParentIdKey("parentId");
        config.setChildrenKey("children");
        config.setWeightKey("sort");
        List<Tree<Long>> tree = TreeUtil.build(filtered, GlobalConstants.ROOT_PARENT_ID, config, (m, node) -> {
            node.setId(m.getId());
            node.setParentId(m.getParentId());
            node.setWeight(m.getSort());
            node.putExtra("title", m.getTitle());
            node.putExtra("type", m.getType());
            node.putExtra("path", m.getPath());
            node.putExtra("name", m.getName());
            node.putExtra("component", m.getComponent());
            node.putExtra("redirect", m.getRedirect());
            node.putExtra("icon", m.getIcon());
            node.putExtra("isExternal", m.getIsExternal());
            node.putExtra("isCache", m.getIsCache());
            node.putExtra("isHidden", m.getIsHidden());
            node.putExtra("permission", m.getPermission());
            node.putExtra("sort", m.getSort());
        });
        return tree.stream().map(this::convertToRoute).collect(Collectors.toList());
    }

    private RouteResp convertToRoute(Tree<Long> tree) {
        RouteResp route = new RouteResp();
        route.setId(tree.getId());
        route.setParentId(tree.getParentId());
        route.setTitle((String) tree.get("title"));
        route.setType((Integer) tree.get("type"));
        route.setPath((String) tree.get("path"));
        route.setName((String) tree.get("name"));
        route.setComponent((String) tree.get("component"));
        route.setRedirect((String) tree.get("redirect"));
        route.setIcon((String) tree.get("icon"));
        route.setIsExternal((Boolean) tree.get("isExternal"));
        route.setIsCache((Boolean) tree.get("isCache"));
        route.setIsHidden((Boolean) tree.get("isHidden"));
        route.setPermission((String) tree.get("permission"));
        route.setSort((Integer) tree.get("sort"));
        if (CollUtil.isNotEmpty(tree.getChildren())) {
            route.setChildren(tree.getChildren().stream()
                .map(this::convertToRoute)
                .collect(Collectors.toList()));
        }
        return route;
    }
}