package com.novik.myblog.repository;

import com.novik.myblog.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Set;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Tag save(Tag tag) {
        String sql =
                "insert into tags (title) values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();



        return null;
    }

    @Override
    public Set<Tag> findTagsByTitleIn(Set<String> titles) {
        return Set.of();
    }
}
