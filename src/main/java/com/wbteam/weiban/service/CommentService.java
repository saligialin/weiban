package com.wbteam.weiban.service;

import com.wbteam.weiban.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    int insertComment(Comment comment);

    int deleteCommentById(String id);

    List<Comment> getCommentByPassage(String passageId);
}
