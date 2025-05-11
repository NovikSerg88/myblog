package com.novik.myblog.service;

import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    void createComment(Long postId, String text);

    void updateComment(Long commentId, String text);

    void deleteComment(Long commentId);
}
