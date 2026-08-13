package com.openplatform.admin.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import com.openplatform.admin.user.model.entity.UserDO;
import com.openplatform.admin.user.service.UserService;
import com.openplatform.common.base.controller.BaseController;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

@Tag(name = "用户管理")
@RestController
@CrudRequestMapping(value = "system/user", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE, Api.EXPORT})
public class UserController extends BaseController<UserService, UserDO, UserDO, UserDO, UserDO> {
}