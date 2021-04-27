package com.wbteam.weiban.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel("返回数据")
public class ResponseData {
    private Integer status;

    private String message;

    private Map<String, Object> data;

    public ResponseData(){}

    public ResponseData(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseData(Integer status, String message, Map<String, Object> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
