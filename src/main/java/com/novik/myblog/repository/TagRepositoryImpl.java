package com.novik.myblog.repository;

import com.novik.myblog.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Tag save(Tag tag) {
        String sql =
                "insert into tags (title) values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, tag.getTitle());
            return ps;
        }, keyHolder);

        Long id = Optional.ofNullable(keyHolder.getKey())
                .orElseThrow(() -> new DataAccessException("Failed to obtain generated ID") {
                })
                .longValue();

        tag.setId(id);

        return tag;
    }

    @Override
    public Optional<Tag> findByTitle(String title) {
        String sql = "SELECT id, title FROM tags WHERE title = ?";

        try {
            Tag tag = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Tag t = new Tag();
                t.setId(rs.getLong("id"));
                t.setTitle(rs.getString("title"));
                return t;
            }, title);

            return Optional.ofNullable(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Long> findTagIdByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public void savePostTags(Long postId, Long tagId) {
        String sql =
                "INSERT INTO post_tags " +
                        "(post_id, tag_id) " +
                        "VALUES (?, ?)";

        jdbcTemplate.update(sql, postId, tagId);
    }

    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(
                rs.getLong("id"),
                rs.getString("title")
        );
    }
}
