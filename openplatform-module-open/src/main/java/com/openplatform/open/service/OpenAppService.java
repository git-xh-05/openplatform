package com.openplatform.open.service;

import com.openplatform.common.base.service.BaseService;
import com.openplatform.open.model.entity.OpenAppDO;
import com.openplatform.open.model.query.OpenAppQuery;
import com.openplatform.open.model.req.OpenAppReq;
import com.openplatform.open.model.resp.OpenAppDetailResp;
import com.openplatform.open.model.resp.OpenAppResp;
import com.openplatform.open.model.resp.OpenAppSecretResp;

public interface OpenAppService extends BaseService<OpenAppResp, OpenAppDetailResp, OpenAppQuery, OpenAppReq> {

    OpenAppSecretResp getSecret(Long id);

    void resetSecret(Long id);

    OpenAppDO getByAccessKey(String accessKey);
}