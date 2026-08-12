package com.openplatform.open.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.openplatform.common.base.controller.BaseController;
import com.openplatform.open.model.query.OpenAppQuery;
import com.openplatform.open.model.req.OpenAppReq;
import com.openplatform.open.model.resp.OpenAppDetailResp;
import com.openplatform.open.model.resp.OpenAppResp;
import com.openplatform.open.model.resp.OpenAppSecretResp;
import com.openplatform.open.service.OpenAppService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

@Tag(name = "应用管理 API")
@RestController
@RequiredArgsConstructor
@CrudRequestMapping(value = "/open-platform/app", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE,
    Api.EXPORT})
public class OpenAppController extends BaseController<OpenAppService, OpenAppResp, OpenAppDetailResp, OpenAppQuery, OpenAppReq> {

    @Operation(summary = "获取密钥", description = "获取应用密钥")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("open-platform:app:secret")
    @GetMapping("/{id}/secret")
    public OpenAppSecretResp getSecret(@PathVariable Long id) {
        return baseService.getSecret(id);
    }

    @Operation(summary = "重置密钥", description = "重置应用密钥")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("open-platform:app:resetSecret")
    @PatchMapping("/{id}/secret")
    public void resetSecret(@PathVariable Long id) {
        baseService.resetSecret(id);
    }
}