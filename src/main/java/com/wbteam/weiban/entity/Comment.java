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
@ApiModel("评论")
@TableName("weiban_comment")
public class Comment {

    @TableId(value = "id")
    @ApiModelProperty(name = "id", value = "ID")
    private String id;

    @ApiModelProperty(name = "passageId", value = "文章ID")
    private String passageId;

    @ApiModelProperty(name = "readerId", value = "阅读者ID")
    private String readerId;

    @ApiModelProperty(name = "context", value = "评论内容")
    private String context;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    @ApiModelProperty(name = "time", value = "评论时间")
    private Date time;
}
