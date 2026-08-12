package com.openplatform.open.service.impl;

import org.springframework.stereotype.Service;
import com.openplatform.common.base.service.BaseServiceImpl;
import com.openplatform.common.context.UserContextHolder;
import com.openplatform.open.enums.SubscribeStatusEnum;
import com.openplatform.open.mapper.SubscribeMapper;
import com.openplatform.open.model.entity.SubscribeDO;
import com.openplatform.open.model.query.SubscribeQuery;
import com.openplatform.open.model.req.SubscribeReq;
import com.openplatform.open.model.resp.SubscribeDetailResp;
import com.openplatform.open.model.resp.SubscribeResp;
import com.openplatform.open.service.SubscribeService;

import java.time.LocalDateTime;

@Service
public class SubscribeServiceImpl extends BaseServiceImpl<SubscribeMapper, SubscribeDO, SubscribeResp, SubscribeDetailResp, SubscribeQuery, SubscribeReq> implements SubscribeService {

    @Override
    public void approve(Long id) {
        SubscribeDO entity = super.getById(id);
        entity.setStatus(SubscribeStatusEnum.APPROVED);
        entity.setApproveUser(UserContextHolder.getUserId());
        entity.setApproveTime(LocalDateTime.now());
        baseMapper.updateById(entity);
    }

    @Override
    public void reject(Long id) {
        SubscribeDO entity = super.getById(id);
        entity.setStatus(SubscribeStatusEnum.REJECTED);
        entity.setApproveUser(UserContextHolder.getUserId());
        entity.setApproveTime(LocalDateTime.now());
        baseMapper.updateById(entity);
    }
}