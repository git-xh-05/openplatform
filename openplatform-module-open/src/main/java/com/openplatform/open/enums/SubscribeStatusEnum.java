package com.openplatform.open.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.continew.starter.core.enums.BaseEnum;

@Getter
@RequiredArgsConstructor
public enum SubscribeStatusEnum implements BaseEnum<Integer> {

    PENDING(0, "待审核"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已拒绝");

    private final Integer value;
    private final String description;
}