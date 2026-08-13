package com.openplatform.open.service;

import com.openplatform.common.base.service.BaseService;
import com.openplatform.open.model.query.ApiLogQuery;
import com.openplatform.open.model.resp.ApiLogDetailResp;
import com.openplatform.open.model.resp.ApiLogResp;

import java.util.List;

public interface ApiLogService extends BaseService<ApiLogResp,ApiLogDetailResp,ApiLogQuery,Void> {
}