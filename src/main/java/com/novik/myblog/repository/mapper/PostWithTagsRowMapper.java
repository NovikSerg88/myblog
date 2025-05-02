package com.novik.myblog.repository.mapper;

import com.novik.myblog.model.Post;
import com.novik.myblog.model.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class PostWithTagsRowMapper implements ResultSetExtractor<List<Post>> {


    @Override
    public List<Post> extractData(ResultSet rs) throws SQLException {
        Map<Long, Post> postsMap = new HashMap<>();

        while (rs.next()) {
            Long postId = rs.getLong("p_id");

            Post post = postsMap.get(postId);
            if (post == null) {
                post = new Post();
                post.setId(postId);
                post.setTitle(rs.getString("p_title"));
                post.setContent(rs.getString("p_content"));
                post.setImageUrl(rs.getString("p_imageUrl"));
                post.setLikesCount(rs.getInt("p_likesCount"));
                post.setCommentsCount(rs.getInt("comment_count"));
                post.setTags(new HashSet<>());
                postsMap.put(postId, post);
            }

            // Добавляем тег, если он есть
            Long tagId = rs.getLong("t_id");
            if (!rs.wasNull()) {
                Tag tag = new Tag();
                tag.setId(tagId);
                tag.setTitle(rs.getString("t_title"));
                post.getTags().add(tag);
            }
        }

        return new ArrayList<>(postsMap.values());
    }
}
