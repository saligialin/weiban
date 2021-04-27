package com.wbteam.weiban.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbteam.weiban.entity.Comment;
import com.wbteam.weiban.mapper.CommentMapper;
import com.wbteam.weiban.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int insertComment(Comment comment) {
        try {
            comment.setId(UUID.randomUUID().toString());
            comment.setTime(new Date());
            return commentMapper.insert(comment);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public int deleteCommentById(String id) {
        try {
            return commentMapper.deleteById(id);
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

    @Override
    public List<Comment> getCommentByPassage(String passageId) {
        try {
            QueryWrapper<Comment> wrapper = new QueryWrapper<>();
            wrapper.eq("passage_id",passageId);
            return commentMapper.selectList(wrapper);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    @Override
    public List<Comment> getCommentByPage(String passageId, Integer currPage, Integer pageSize) {
        try {
            Page<Comment> page = new Page<>(currPage, pageSize);
            QueryWrapper<Comment> wrapper = new QueryWrapper<>();
            wrapper.eq("passage_id",passageId);
            Page<Comment> commentPage = commentMapper.selectPage(page, wrapper);
            return commentPage.getRecords();
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
}
