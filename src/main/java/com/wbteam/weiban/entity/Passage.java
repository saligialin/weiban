package com.wbteam.weiban.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wbteam.weiban.entity.enums.PassageKind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("文章")
@TableName("weiban_passage")
public class Passage {

    @TableId(value = "id")
    @ApiModelProperty(name = "id", value = "ID")
    private String id;

    @ApiModelProperty(name = "cover", value = "封面图URL")
    private String cover;

    @ApiModelProperty(name = "title", value = "标题")
    private String title;

    @ApiModelProperty(name = "AuthorId", value = "作者ID")
    private String AuthorId;

    @ApiModelProperty(name = "context", value = "正文")
    private String context;

    @ApiModelProperty(name = "time", value = "发布时间")
    private Date time;

    @ApiModelProperty(name = "score", value = "综合得分")
    private Double score;

    @ApiModelProperty(name = "kindId", value = "文章类型")
    private PassageKind kindId;
}