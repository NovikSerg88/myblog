package com.novik.myblog.repository;

import com.novik.myblog.model.Post;
import com.novik.myblog.repository.mapper.PostWithCommentsAndTagsRowMapper;
import com.novik.myblog.repository.mapper.PostWithTagsRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long save(Post post) {
        String sql =
                "INSERT INTO posts (title, content, image_url, likes_count, comments_count) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getImageUrl());
            ps.setInt(4, post.getLikesCount());
            ps.setInt(5, post.getCommentsCount());
            return ps;
        }, keyHolder);

        Long id = Optional.ofNullable(keyHolder.getKey())
                .orElseThrow(() -> new DataAccessException("Failed to obtain generated ID") {
                })
                .longValue();

        post.setId(id);
        return post.getId();
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql =
                "SELECT DISTINCT " +
                        "p.id as p_id, " +
                        "p.title as p_title, " +
                        "p.content as p_content, " +
                        "p.image_url as p_imageUrl, " +
                        "p.likes_count as p_likeCount, " +
                        "c.id as c_id, " +
                        "c.content as c_content, " +
                        "t.id as t_id, " +
                        "t.title as t_title, " +
                        "(SELECT COUNT(*) FROM comments WHERE post_id = p.id) as comment_count " +
                        "FROM posts p " +
                        "LEFT JOIN comments c ON c.post_id = p.id " +
                        "LEFT JOIN post_tags pt ON p.id = pt.post_id " +
                        "LEFT JOIN tags t ON pt.tag_id = t.id " +
                        "WHERE p.id = ?";

        try {
            Post post = jdbcTemplate.query(
                    sql,
                    new PostWithCommentsAndTagsRowMapper(),
                    id
            );
            return Optional.ofNullable(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Post> findAllPostsWithTags(int limit, int offset) {
        String sql =
                "SELECT " +
                        "p.id as p_id, " +
                        "p.title as p_title, " +
                        "p.content as p_content, " +
                        "p.image_url as p_imageUrl, " +
                        "p.likes_count as p_likesCount, " +
                        "t.id as t_id, " +
                        "t.title as t_title, " +
                        "(SELECT COUNT(*) FROM comments WHERE post_id = p.id) as comment_count " +
                        "FROM " + "( " +
                        "SELECT " +
                        "id, title, content, image_url, likes_count " +
                        "FROM " + "posts" + " " +
                        "ORDER BY id LIMIT ? OFFSET ?)" + " as p " +
                        "LEFT JOIN post_tags as pt ON p.id = pt.post_id " +
                        "LEFT JOIN tags as t ON pt.tag_id = t.id ";

        return jdbcTemplate.query(sql, new PostWithTagsRowMapper(), limit, offset);
    }

    @Override
    public List<Post> findByTagIds(List<Long> tagIds, int limit, int offset) {
        String inSql = String.join(",", Collections.nCopies(tagIds.size(), "?"));

        String sql =
                "SELECT " +
                        "p.id as p_id, " +
                        "p.title as p_title, " +
                        "p.content as p_content, " +
                        "p.image_url as p_imageUrl, " +
                        "p.likes_count as p_likesCount, " +
                        "t.id as t_id, " +
                        "t.title as t_title, " +
                        "(SELECT COUNT(*) FROM comments WHERE post_id = p.id) as comment_count " +
                        "FROM " + "( " +
                        "SELECT " +
                        "id, title, content, image_url, likes_count " +
                        "FROM " + "posts" + " " +
                        "ORDER BY id LIMIT ? OFFSET ?)" + " as p " +
                        "LEFT JOIN post_tags as pt ON p.id = pt.post_id " +
                        "LEFT JOIN tags as t ON pt.tag_id = t.id " +
                        "WHERE t.id IN (" + inSql + ") ";

        Object[] params = new Object[tagIds.size() + 2];
        params[0] = limit;
        params[1] = offset;
        for (int i = 0; i < tagIds.size(); i++) {
            params[i + 2] = tagIds.get(i);
        }

        return jdbcTemplate.query(sql, new PostWithTagsRowMapper(), params);
    }

    @Override
    public void deletePostById(Long id) {
        String sql =
                "DELETE FROM " + "posts" + " " +
                        "WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public int update(Post post) {
        String sql =
                "UPDATE " + "posts" + " " +
                        "SET " +
                        "title = ?, " +
                        "content = ?, " +
                        "image_url = ?, " +
                        "likes_count = ? " +
                        "WHERE id = ?";

        return jdbcTemplate.update(sql,
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getLikesCount(),
                post.getId());
    }
}
