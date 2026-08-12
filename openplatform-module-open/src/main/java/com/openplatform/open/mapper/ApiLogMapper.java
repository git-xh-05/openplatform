package com.openplatform.open.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.openplatform.common.base.mapper.DataPermissionMapper;
import com.openplatform.open.model.entity.ApiLogDO;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface ApiLogMapper extends DataPermissionMapper<ApiLogDO> {

    List<LinkedHashMap<String, Object>> statByApi();

    List<LinkedHashMap<String, Object>> statByApp();

    List<LinkedHashMap<String, Object>> statTrend(@Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);
}