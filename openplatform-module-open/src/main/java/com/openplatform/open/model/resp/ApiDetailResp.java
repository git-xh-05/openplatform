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

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.openplatform.common.base.model.resp.BaseDetailResp;
import com.openplatform.common.enums.DisEnableStatusEnum;
import top.continew.starter.excel.converter.ExcelBaseEnumConverter;

import java.io.Serial;

@Data
@ExcelIgnoreUnannotated
@Schema(description = "API 详情响应参数")
public class ApiDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称", example = "查询用户")
    @ExcelProperty(value = "名称", order = 2)
    private String name;

    @Schema(description = "路径", example = "/api/user/list")
    @ExcelProperty(value = "路径", order = 3)
    private String path;

    @Schema(description = "请求方法", example = "GET")
    @ExcelProperty(value = "请求方法", order = 4)
    private String method;

    @Schema(description = "后端服务地址", example = "http://localhost:8081/user/list")
    @ExcelProperty(value = "服务地址", order = 5)
    private String serviceUrl;

    @Schema(description = "状态", example = "1")
    @ExcelProperty(value = "状态", converter = ExcelBaseEnumConverter.class, order = 6)
    private DisEnableStatusEnum status;

    @Schema(description = "描述", example = "查询用户列表")
    @ExcelProperty(value = "描述", order = 7)
    private String description;
}