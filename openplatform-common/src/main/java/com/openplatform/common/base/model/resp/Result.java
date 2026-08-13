package com.openplatform.common.base.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "通用响应结果")
public record Result<T>(@Schema(description = "状态码") int code,
                        @Schema(description = "消息") String msg,
                        @Schema(description = "数据") T data) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static <T> Result<T> ok(T data) {
        return new Result<>(0, "success", data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(0, "success", null);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(1, msg, null);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }
}