package com.wbteam.weiban.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum PassageKind {
    POPULAR_SCIENCE(101,"科普文");

    @EnumValue
    private final Integer id;

    private final String kind;

    PassageKind(Integer id, String kind) {
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
