package com.wbteam.weiban.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wbteam.weiban.entity.enums.RelationKind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("绑定关系")
@TableName("weiban_bind")
public class Bind {

    @TableId(value = "id")
    @ApiModelProperty(name = "id", value = "ID")
    private String id;

    @ApiModelProperty(name = "elderId", value = "老人ID")
    private String elderId;

    @ApiModelProperty(name = "youthId", value = "家属/护工ID")
    private String youthId;

    @ApiModelProperty(name = "relationId", value = "关系类型")
    private RelationKind relationId;

    @ApiModelProperty(name = "isAccepted", value = "老人是否接收")
    private Integer isAccepted;
}
