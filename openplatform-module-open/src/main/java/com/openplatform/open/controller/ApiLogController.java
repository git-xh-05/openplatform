package com.openplatform.open.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import com.openplatform.common.base.controller.BaseController;
import com.openplatform.open.model.query.ApiLogQuery;
import com.openplatform.open.model.resp.ApiLogDetailResp;
import com.openplatform.open.model.resp.ApiLogResp;
import com.openplatform.open.service.ApiLogService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

@Tag(name = "调用日志")
@RequiredArgsConstructor
@RestController
@CrudRequestMapping(value = "/open-platform/log",api = {Api.PAGE, Api.GET})
public class ApiLogController extends BaseController<ApiLogService,ApiLogResp, ApiLogDetailResp,ApiLogQuery,Void> {
}