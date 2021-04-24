package com.wbteam.weiban.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum RelationKind {
    FATHER_AND_SON(101,"父子"),
    MOTHER_AND_SON(102,"母子"),
    MOTHER_AND_DAUGHTER(103,"母女"),
    FATHER_AND_DAUGHTER(104,"父女"),
    OTHER_RELATION(105,"老人与社区护工")
    ;

    @EnumValue
    private final Integer id;

    private final String relation;

    RelationKind(Integer id, String relation) {
        this.id = id;
        this.relation = relation;
    }

    public Integer getId() {
        return id;
    }

    public String getRelation() {
        return relation;
    }
}
