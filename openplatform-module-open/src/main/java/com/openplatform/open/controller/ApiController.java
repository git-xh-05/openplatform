/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openplatform.open.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import com.openplatform.common.base.controller.BaseController;
import com.openplatform.open.model.query.ApiQuery;
import com.openplatform.open.model.req.ApiReq;
import com.openplatform.open.model.resp.ApiDetailResp;
import com.openplatform.open.model.resp.ApiResp;
import com.openplatform.open.service.ApiService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

@Tag(name = "API管理")
@RestController
@RequiredArgsConstructor
@CrudRequestMapping(value = "open-platform/api", api = {Api.PAGE, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE,
    Api.EXPORT})
public class ApiController extends BaseController<ApiService, ApiResp, ApiDetailResp, ApiQuery, ApiReq> {
}
