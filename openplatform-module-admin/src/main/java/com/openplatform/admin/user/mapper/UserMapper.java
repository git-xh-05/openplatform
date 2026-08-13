package com.openplatform.admin.user.mapper;

import com.openplatform.admin.user.model.entity.UserDO;
import com.openplatform.common.base.mapper.DataPermissionMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends DataPermissionMapper<UserDO> {

    @Select("SELECT * FROM sys_user WHERE deleted = 0 AND username = #{username}")
    UserDO selectByUsername(@Param("username") String username);
}