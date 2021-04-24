package com.wbteam.weiban.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum MemoKind {
    REMINDER_TO_TAKE_MEDICINE(101,"吃药");

    @EnumValue
    private final Integer id;

    private final String kind;

    MemoKind(Integer id, String kind) {
        this.id = id;
        this.kind = kind;
    }

    public Integer getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }
}
