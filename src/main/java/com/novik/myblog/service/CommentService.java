package com.novik.myblog.service;

import com.novik.myblog.model.Comment;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    void createComment(Long postId, String text);

    void deletePostComments(Long postId);

    void updateComment(Long commentId, String text);

    void deleteComment(Long commentId);
}
