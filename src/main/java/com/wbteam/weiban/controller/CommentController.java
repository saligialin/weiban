package com.wbteam.weiban.controller;

import com.wbteam.weiban.annotation.ApiJsonObject;
import com.wbteam.weiban.annotation.ApiJsonProperty;
import com.wbteam.weiban.entity.*;
import com.wbteam.weiban.entity.enums.ResponseStates;
import com.wbteam.weiban.service.CarerService;
import com.wbteam.weiban.service.ChildService;
import com.wbteam.weiban.service.CommentService;
import com.wbteam.weiban.service.ElderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "文章评论接口")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ChildService childService;

    @Autowired
    private CarerService carerService;

    @Autowired
    private ElderService elderService;

    /**
     *
     * @param comment
     * @return
     */
    @ApiOperation("增加评论")
    @PostMapping("/add")
    public ResponseData add(@RequestBody Comment comment){
        int insertComment = commentService.insertComment(comment);
        if (insertComment>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("删除评论")
    @DeleteMapping("/delete")
    public ResponseData delete(@ApiJsonObject(name = "commentDelete", value = @ApiJsonProperty(key = "id", example = "评论ID"))@RequestBody Map<String, String> parameter) {
        String id = parameter.get("id");
        if (id==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        int deleteComment = commentService.deleteCommentById(id);
        if (deleteComment>0) return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage());
        else return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("获取文章评论")
    @PostMapping("/getComments")
    public ResponseData getComments(@ApiJsonObject(name = "commentsGet", value = @ApiJsonProperty(key = "passageId", example = "文章ID")) @RequestBody Map<String, String> parameter) {
        String passageId = parameter.get("passageId");
        if (passageId==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Comment> comments = commentService.getCommentByPassage(passageId);
        if (comments==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Comment comment : comments) {
            Map<String, Object> res = new HashMap<>();
            res.put("comment",comment);
            Child child = childService.selectById(comment.getReaderId());
            if (child!=null) {
                res.put("reader",child);
                res.put("readerRole",2);
            } else {
                Carer carer = carerService.selectById(comment.getReaderId());
                if (carer!=null) {
                    res.put("reader",carer);
                    res.put("readerRole",3);
                } else {
                    Elder elder = elderService.selectById(comment.getReaderId());
                    if (elder!=null) {
                        res.put("author", elder);
                        res.put("readerRole",1);
                    } else {
                        res.put("reader", null);
                        res.put("readerRole",0);
                    }
                }
            }
            list.add(res);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("result",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @ApiOperation("分页获取文章评论")
    @PostMapping("/getCommentsByPage")
    public ResponseData getCommentsByPage(@ApiJsonObject(name = "commentsGetByPage", value = {
            @ApiJsonProperty(key = "passageId", example = "文章ID"),
            @ApiJsonProperty(key = "currPage", example = "请求页数"),
            @ApiJsonProperty(key = "pageSize", example = "页面尺寸")
    })@RequestBody Map<String, Object> parameter) {
        String passageId = (String)parameter.get("passageId");
        Integer currPage = (Integer)parameter.get("currPage");
        Integer pagesize = (Integer)parameter.get("pagesize");
        if (passageId==null||currPage==null||pagesize==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Comment> comments = commentService.getCommentByPage(passageId, currPage, pagesize);
        if (comments==null) return new ResponseData(ResponseStates.ERROR.getValue(), ResponseStates.ERROR.getMessage());
        List<Map<String,Object>> list = new ArrayList<>();
        for (Comment comment : comments) {
            Map<String, Object> res = new HashMap<>();
            res.put("comment",comment);
            Child child = childService.selectById(comment.getReaderId());
            if (child!=null) {
                res.put("reader",child);
                res.put("readerRole",2);
            } else {
                Carer carer = carerService.selectById(comment.getReaderId());
                if (carer!=null) {
                    res.put("reader",carer);
                    res.put("readerRole",3);
                } else {
                    Elder elder = elderService.selectById(comment.getReaderId());
                    if (elder!=null){
                        res.put("author", elder);
                        res.put("readerRole",1);
                    } else {
                        res.put("reader", null);
                        res.put("readerRole",0);
                    }
                }
            }
            list.add(res);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("result",list);
        return new ResponseData(ResponseStates.SUCCESS.getValue(), ResponseStates.SUCCESS.getMessage(), data);
    }
}
