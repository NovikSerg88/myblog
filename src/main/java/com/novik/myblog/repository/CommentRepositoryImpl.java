package com.novik.myblog.repository;

import com.novik.myblog.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void deleteAllByPostId(long postId) {
        String sql = "DELETE FROM comments WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

    @Override
    public Comment save(Comment comment) {
        String sql = "INSERT INTO comments (content, post_id) VALUES (?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                comment.getText(),
                comment.getPostId());
        comment.setId(id);
        return comment;
    }

    public Optional<Comment> findById(Long id) {
        String sql = "SELECT * FROM comments WHERE id = ?";
        try {
            Comment comment = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Comment c = new Comment();
                c.setId(rs.getLong("id"));
                c.setText(rs.getString("content"));
                c.setPostId(rs.getLong("post_id"));
                return c;
            });
            return Optional.ofNullable(comment);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    public void update(Comment comment) {
        String sql = "UPDATE comments SET content = ? WHERE id = ?";
        jdbcTemplate.update(sql, comment.getText(), comment.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM comments WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
