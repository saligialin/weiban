package com.wbteam.weiban.entity.enums;

public enum ResponseStates {
    SUCCESS("操作成功",200),
    EXISTED_ACCOUNT("用户已存在", 601),
    UNKNOWN_ACCOUNT("用户不存在", 602),
    PASSWORD_INCORRECT("密码错误", 603),
    LOCKED_ACCOUNT("此账户已被禁用",604),
    AUTHORITY_INSUFFICIENT("用户无此权限",605),
    MESSAGE_CODE_INCORRECT("短信验证码错误",606),
    IMAGE_FORMAT_ERROR("图片格式错误",607),
    ERROR("错误",100);

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
