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

package com.openplatform.open.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import com.openplatform.common.enums.DisEnableStatusEnum;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "API创建或修改请求参数")
public class ApiReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称", example = "查询用户")
    @NotBlank(message = "名称不能为空")
    @Length(max = 100, message = "名称长度不能超过100个字符")
    private String name;

    @Schema(description = "路径", example = "/api/user/list")
    @NotBlank(message = "路径不能为空")
    private String path;

    @Schema(description = "请求方法", example = "GET")
    @NotBlank(message = "请求方法不能为空")
    private String method;

    @Schema(description = "后端服务地址", example = "http://localhost:8081/user/list")
    @NotBlank(message = "服务器地址不能为空")
    private String serviceUrl;

    @Schema(description = "描述", example = "查询用户列表")
    @Length(max = 200, message = "描述长度不能超过200个字符")
    private String description;

    @Schema(description = "状态", example = "1")
    private DisEnableStatusEnum status;
}
