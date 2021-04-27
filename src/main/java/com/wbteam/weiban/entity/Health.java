package com.wbteam.weiban.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("健康信息")
@TableName("weiban_health")
public class Health {

    @TableId(value = "id")
    @ApiModelProperty(name = "id", value = "ID")
    private String id;

    @ApiModelProperty(name = "elderId", value = "老人ID")
    private String elderId;

    @ApiModelProperty(name = "height", value = "身高")
    private Double height;

    @ApiModelProperty(name = "weight", value = "体重")
    private Double weight;

    @ApiModelProperty(name = "bloodPressure", value = "血压")
    private Double bloodPressure;

    @ApiModelProperty(name = "heartRate", value = "心率")
    private Integer heartRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(name = "time", value = "时间")
    private Date time;
}
