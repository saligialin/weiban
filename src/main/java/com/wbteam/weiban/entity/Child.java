package com.wbteam.weiban.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("家属")
@TableName("weiban_child")
public class Child {

    @TableId(value = "id")
    @ApiModelProperty(name = "id", value = "ID")
    private String id;

    @ApiModelProperty(name = "tel", value = "手机号")
    private String tel;

    @JsonBackReference("密码")
    @ApiModelProperty(name = "password", value = "密码")
    private String password;

    @ApiModelProperty(name = "picture", value = "头像URL")
    private String picture;

    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    @ApiModelProperty(name = "gender", value = "性别")
    private Integer gender;


}
