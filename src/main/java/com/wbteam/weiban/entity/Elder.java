package com.wbteam.weiban.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@ApiModel("老人用户")
@TableName("weiban_elder")
public class Elder {

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "birthday", value = "生日")
    private Date birthday;

    @ApiModelProperty(name = "address", value = "地址")
    private String address;

    @ApiModelProperty(name = "place", value = "位置名")
    private String place;

}
