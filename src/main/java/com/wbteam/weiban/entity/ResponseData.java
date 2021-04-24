package com.wbteam.weiban.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel("返回数据")
public class ResponseData {
    private Integer code;

    private String message;

    private Map<String, Object> data;

    public ResponseData(){}

    public ResponseData(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseData(Integer code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
