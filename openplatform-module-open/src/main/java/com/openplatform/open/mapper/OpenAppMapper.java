package com.openplatform.open.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.openplatform.common.base.mapper.DataPermissionMapper;
import com.openplatform.open.model.entity.OpenAppDO;
import top.continew.starter.encrypt.field.annotation.FieldEncrypt;

@Mapper
public interface OpenAppMapper extends DataPermissionMapper<OpenAppDO> {

    @Select("select * from open_platform_app where deleted = 0 AND access_key = #{accessKey}")
    OpenAppDO selectByAccessKey(@FieldEncrypt @Param("accessKey") String accessKey);
}