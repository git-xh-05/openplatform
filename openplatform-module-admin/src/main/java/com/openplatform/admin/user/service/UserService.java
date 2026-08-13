package com.openplatform.admin.user.service;

import com.openplatform.admin.user.model.entity.UserDO;
import com.openplatform.common.base.service.BaseService;

public interface UserService extends BaseService<UserDO, UserDO, UserDO, UserDO> {

    UserDO getByUsername(String username);

    UserDO getById(Long id);
}