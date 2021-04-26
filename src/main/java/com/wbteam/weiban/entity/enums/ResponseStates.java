package com.wbteam.weiban.entity.enums;

public enum ResponseStates {
    SUCCESS("操作成功",200),
    ERROR("错误",100),
    EXISTED_ACCOUNT("用户已存在", 101),
    UNKNOWN_ACCOUNT("用户不存在", 602),
    PASSWORD_INCORRECT("密码错误", 103),
    MESSAGE_CODE_INCORRECT("短信验证码错误",104),
    TOKEN_NOT_PROVIDE("未提供Token",105),
    TOKEN_IS_ERROR("Token错误",106),
    TOKEN_IS_EXPIRED("Token过期",107),
    ROLE_IS_ERROR("用户角色选择错误",108)
    ;

    private final String message;
    private final int value;

    ResponseStates(String message, int value){
        this.message=message;
        this.value=value;
    }
    public String getMessage(){
        return this.message;
    }
    public int getValue(){
        return this.value;
    }
}
