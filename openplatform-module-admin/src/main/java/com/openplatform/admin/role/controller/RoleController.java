package com.openplatform.admin.role.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import com.openplatform.admin.role.model.entity.RoleDO;
import com.openplatform.admin.role.service.RoleService;
import com.openplatform.common.base.controller.BaseController;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

@Tag(name = "角色管理")
@RestController
@CrudRequestMapping(value = "system/role", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE, Api.EXPORT})
public class RoleController extends BaseController<RoleService, RoleDO, RoleDO, RoleDO, RoleDO> {
}