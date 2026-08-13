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

package com.openplatform.open.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.openplatform.common.base.model.resp.BaseDetailResp;
import com.openplatform.common.enums.DisEnableStatusEnum;

import java.io.Serial;

@Data
@Schema(description = "API响应参数")
public class ApiResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称", example = "查询用户")
    private String name;

    @Schema(description = "路径", example = "/api/user/list")
    private String path;

    @Schema(description = "请求方法", example = "GET")
    private String method;

    @Schema(description = "后端服务地址", example = "http:/?localhost:8081/user/list")
    private String serviceUrl;

    @Schema(description = "状态", example = "1")
    private DisEnableStatusEnum status;

    @Schema(description = "描述", example = "查询用户列表")
    private String description;
}
