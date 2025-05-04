package com.novik.myblog.repository;

public interface CommentRepository {
    void deleteAllByPostId(long postId);
}
