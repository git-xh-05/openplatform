package com.openplatform.admin.menu.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import com.openplatform.admin.menu.model.entity.MenuDO;
import com.openplatform.admin.menu.service.MenuService;
import com.openplatform.common.base.controller.BaseController;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

@Tag(name = "菜单管理")
@RestController
@CrudRequestMapping(value = "system/menu", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE, Api.EXPORT})
public class MenuController extends BaseController<MenuService, MenuDO, MenuDO, MenuDO, MenuDO> {
}