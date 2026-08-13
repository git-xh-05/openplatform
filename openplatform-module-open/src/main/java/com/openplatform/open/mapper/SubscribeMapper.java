package com.openplatform.open.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.openplatform.common.base.mapper.DataPermissionMapper;
import com.openplatform.open.model.entity.SubscribeDO;

@Mapper
public interface SubscribeMapper extends DataPermissionMapper<SubscribeDO> {

    @Select("select * from open_subscribe where deleted = 0 AND app_id = #{appId} AND api_id = #{apiId} AND status = 1 LIMIT 1")
    SubscribeDO selectByAppIdAndApiId(@Param("appId") Long appId, @Param("apiId") Long apiId);
}