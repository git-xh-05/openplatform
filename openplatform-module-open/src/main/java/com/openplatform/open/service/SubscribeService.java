package com.openplatform.open.service;

import com.openplatform.common.base.service.BaseService;
import com.openplatform.open.model.query.SubscribeQuery;
import com.openplatform.open.model.req.SubscribeReq;
import com.openplatform.open.model.resp.SubscribeDetailResp;
import com.openplatform.open.model.resp.SubscribeResp;

public interface SubscribeService extends BaseService<SubscribeResp, SubscribeDetailResp, SubscribeQuery, SubscribeReq> {

    void approve(Long id);

    void reject(Long id);
}