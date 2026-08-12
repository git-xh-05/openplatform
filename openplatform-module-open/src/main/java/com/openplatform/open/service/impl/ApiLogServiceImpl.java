package com.openplatform.open.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.openplatform.common.base.service.BaseServiceImpl;
import com.openplatform.open.mapper.ApiLogMapper;
import com.openplatform.open.model.entity.ApiLogDO;
import com.openplatform.open.model.query.ApiLogQuery;
import com.openplatform.open.model.resp.ApiLogDetailResp;
import com.openplatform.open.model.resp.ApiLogResp;
import com.openplatform.open.service.ApiLogService;

@Service
@RequiredArgsConstructor
public class ApiLogServiceImpl extends BaseServiceImpl<ApiLogMapper,ApiLogDO,ApiLogResp,ApiLogDetailResp,ApiLogQuery,Void> implements ApiLogService {

    @Override
    protected QueryWrapper<ApiLogDO> buildQueryWrapper(ApiLogQuery query) {
        QueryWrapper<ApiLogDO> queryWrapper = super.buildQueryWrapper(query);
        queryWrapper.ge(query.getStartTime() != null, "create_time", query.getStartTime());
        queryWrapper.le(query.getEndTime() != null, "create_time", query.getEndTime());
        queryWrapper.orderByDesc("id");
        return queryWrapper;
    }
}