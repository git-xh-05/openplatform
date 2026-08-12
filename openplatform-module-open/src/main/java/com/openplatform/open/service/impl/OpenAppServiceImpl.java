package com.openplatform.open.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.openplatform.common.base.service.BaseServiceImpl;
import com.openplatform.open.mapper.OpenAppMapper;
import com.openplatform.open.model.entity.OpenAppDO;
import com.openplatform.open.model.query.OpenAppQuery;
import com.openplatform.open.model.req.OpenAppReq;
import com.openplatform.open.model.resp.OpenAppDetailResp;
import com.openplatform.open.model.resp.OpenAppResp;
import com.openplatform.open.model.resp.OpenAppSecretResp;
import com.openplatform.open.service.OpenAppService;
import top.continew.starter.core.constant.StringConstants;

@Service
public class OpenAppServiceImpl extends BaseServiceImpl<OpenAppMapper, OpenAppDO, OpenAppResp, OpenAppDetailResp, OpenAppQuery, OpenAppReq> implements OpenAppService {

    @Override
    public void beforeCreate(OpenAppReq req) {
        req.setAccessKey(Base64.encode(IdUtil.fastSimpleUUID())
            .replace(StringConstants.SLASH, StringConstants.EMPTY)
            .replace(StringConstants.PLUS, StringConstants.EMPTY)
            .substring(0, 30));
        req.setSecretKey(this.generateSecret());
    }

    @Override
    public OpenAppSecretResp getSecret(Long id) {
        OpenAppDO app = super.getById(id);
        OpenAppSecretResp resp = new OpenAppSecretResp();
        resp.setAccessKey(app.getAccessKey());
        resp.setSecretKey(app.getSecretKey());
        return resp;
    }

    @Override
    public void resetSecret(Long id) {
        super.getById(id);
        OpenAppDO app = new OpenAppDO();
        app.setSecretKey(this.generateSecret());
        baseMapper.update(app, Wrappers.lambdaQuery(OpenAppDO.class).eq(OpenAppDO::getId, id));
    }

    @Override
    public OpenAppDO getByAccessKey(String accessKey) {
        return baseMapper.selectByAccessKey(accessKey);
    }

    private String generateSecret() {
        return Base64.encode(IdUtil.fastSimpleUUID())
            .replace(StringConstants.SLASH, StringConstants.EMPTY)
            .replace(StringConstants.PLUS, StringConstants.EMPTY);
    }
}