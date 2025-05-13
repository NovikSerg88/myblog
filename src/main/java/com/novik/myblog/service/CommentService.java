package com.novik.myblog.service;

import com.novik.myblog.dto.CommentDto;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

    CommentDto createComment(Long postId, String text);

    void updateComment(Long commentId, String text);

    void deleteComment(Long commentId);
}
