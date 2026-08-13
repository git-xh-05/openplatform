package com.openplatform.open.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.openplatform.common.base.controller.BaseController;
import com.openplatform.open.model.query.SubscribeQuery;
import com.openplatform.open.model.req.SubscribeReq;
import com.openplatform.open.model.resp.SubscribeDetailResp;
import com.openplatform.open.model.resp.SubscribeResp;
import com.openplatform.open.service.SubscribeService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

@Tag(name = "订阅管理")
@RestController
@RequiredArgsConstructor
@CrudRequestMapping(value = "open-platform/subscribe", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE})
public class SubscribeController extends BaseController<SubscribeService, SubscribeResp, SubscribeDetailResp, SubscribeQuery, SubscribeReq> {

    @Operation(summary = "审核通过", description = "审核通过订阅申请")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("open-platform:subscribe:approve")
    @PatchMapping("/{id}/approve")
    public void approve(@PathVariable Long id) {
        baseService.approve(id);
    }

    @Operation(summary = "审核拒绝", description = "审核拒绝订阅申请")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("open-platform:subscribe:reject")
    @PatchMapping("/{id}/reject")
    public void reject(@PathVariable Long id) {
        baseService.reject(id);
    }
}