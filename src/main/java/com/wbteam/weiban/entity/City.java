package com.wbteam.weiban.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("城市")
@TableName("weiban_city")
public class City {

    @TableId(value = "id")
    @ApiModelProperty(name = "cityName", value = "城市名")
    private String cityName;

    @ApiModelProperty(name = "adCode", value = "地区编号")
    private String adCode;

    @ApiModelProperty(name = "cityCode", value = "城市编号")
    private String cityCode;
}
