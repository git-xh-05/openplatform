package com.openplatform.admin.user.service.impl;

import org.springframework.stereotype.Service;
import com.openplatform.admin.user.mapper.UserMapper;
import com.openplatform.admin.user.model.entity.UserDO;
import com.openplatform.admin.user.service.UserService;
import com.openplatform.common.base.service.BaseServiceImpl;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserDO, UserDO, UserDO, UserDO, UserDO> implements UserService {

    @Override
    public UserDO getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public UserDO getById(Long id) {
        return baseMapper.selectById(id);
    }
}