package com.wbteam.weiban.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Company {
    WEI_BAN_COMPANY(1,"微伴");

    @EnumValue
    private Integer id;

    private String company;

    Company(Integer id, String company) {
        this.id = id;
        this.company = company;
    }

    public Integer getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }
}
