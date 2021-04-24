package com.wbteam.weiban.entity.enums;

public enum User {
    ELDER("elder"),
    CHILD("child"),
    CARER("carer")
    ;

    private final String role;

    public String getRole() {
        return role;
    }

    User(String role) {
        this.role = role;
    }
}
