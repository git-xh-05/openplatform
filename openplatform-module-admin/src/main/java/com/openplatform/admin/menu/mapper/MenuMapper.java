package com.openplatform.admin.menu.mapper;

import com.openplatform.admin.menu.model.entity.MenuDO;
import com.openplatform.common.base.mapper.DataPermissionMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper extends DataPermissionMapper<MenuDO> {

    @Select("SELECT DISTINCT m.* FROM sys_menu m JOIN sys_role_menu rm ON m.id = rm.menu_id JOIN sys_user_role ur ON rm.role_id = ur.role_id WHERE ur.user_id = #{userId} AND m.status = 1 AND m.deleted = 0 ORDER BY m.sort")
    List<MenuDO> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT DISTINCT m.* FROM sys_menu m JOIN sys_role_menu rm ON m.id = rm.menu_id WHERE rm.role_id = #{roleId} AND m.status = 1 AND m.deleted = 0 ORDER BY m.sort")
    List<MenuDO> selectByRoleId(@Param("roleId") Long roleId);

    @Select("SELECT DISTINCT m.permission FROM sys_menu m JOIN sys_role_menu rm ON m.id = rm.menu_id JOIN sys_user_role ur ON rm.role_id = ur.role_id WHERE ur.user_id = #{userId} AND m.permission IS NOT NULL AND m.permission != '' AND m.deleted = 0")
    List<String> selectPermissionByUserId(@Param("userId") Long userId);
}