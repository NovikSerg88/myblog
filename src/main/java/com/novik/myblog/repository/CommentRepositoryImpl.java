package com.novik.myblog.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void deleteAllByPostId(long postId) {
        String sql = "DELETE FROM comments WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }
}
