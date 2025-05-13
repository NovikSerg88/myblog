package com.novik.myblog.repository;

import com.novik.myblog.model.Comment;

import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long commentId);

    void update(Comment comment);

    void deleteById(Long commentId);
}
