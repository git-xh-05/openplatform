package com.openplatform.admin.role.mapper;

import com.openplatform.admin.role.model.entity.RoleDO;
import com.openplatform.common.base.mapper.DataPermissionMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper extends DataPermissionMapper<RoleDO> {

    @Select("SELECT r.* FROM sys_role r JOIN sys_user_role ur ON r.id = ur.role_id WHERE ur.user_id = #{userId} AND r.deleted = 0")
    List<RoleDO> selectByUserId(@Param("userId") Long userId);
}