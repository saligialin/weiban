package com.wbteam.weiban.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wbteam.weiban.entity.enums.MemoKind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("备忘录")
@TableName("weiban_memo")
public class Memo {

    @TableId(value = "id")
    @ApiModelProperty(name = "id", value = "ID")
    private String id;

    @ApiModelProperty(name = "elderId", value = "被提醒ID")
    private String elderId;

    @ApiModelProperty(name = "youthId", value = "添加入ID")
    private String youthId;

    @ApiModelProperty(name = "time", value = "提醒时间")
    private Date time;

    @ApiModelProperty(name = "context", value = "提醒内容")
    private String context;

    @ApiModelProperty(name = "kindId", value = "备忘录类型")
    private MemoKind kindId;
}
