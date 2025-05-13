package com.novik.myblog.service;

import com.novik.myblog.dto.CommentDto;
import com.novik.myblog.model.Comment;
import com.novik.myblog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentDto createComment(Long postId, String text) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setText(text);
        Comment savedComment = commentRepository.save(comment);
        return CommentDto.builder()
                .id(savedComment.getId())
                .content(savedComment.getText())
                .build();
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, String text) {
        commentRepository.findById(commentId).ifPresent(comment -> {
            comment.setText(text);
            commentRepository.update(comment);
        });
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
