package com.novik.myblog.repository.mapper;

import com.novik.myblog.model.Comment;
import com.novik.myblog.model.Post;
import com.novik.myblog.model.Tag;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class PostWithCommentsAndTagsRowMapper implements ResultSetExtractor<Post> {
    @Override
    public Post extractData(ResultSet rs) throws SQLException {
        Post post = null;
        Map<Long, Comment> comments = new HashMap<>();
        Map<Long, Tag> tags = new HashMap<>();

        while (rs.next()) {
            if (post == null) {
                post = new Post();
                post.setId(rs.getLong("p_id"));
                post.setTitle(rs.getString("p_title"));
                post.setContent(rs.getString("p_content"));
                post.setImageUrl(rs.getString("p_imageUrl"));
                post.setLikesCount(rs.getInt("p_likeCount"));
                post.setCommentsCount(rs.getInt("comment_count"));
            }

            long commentId = rs.getLong("c_id");
            if (!rs.wasNull() && !comments.containsKey(commentId)) {
                Comment comment = new Comment();
                comment.setId(commentId);
                comment.setText(rs.getString("c_content"));
                comments.put(commentId, comment);
            }

            long tagId = rs.getLong("t_id");
            if (!rs.wasNull() && !tags.containsKey(tagId)) {
                Tag tag = new Tag();
                tag.setId(tagId);
                tag.setTitle(rs.getString("t_title"));
                tags.put(tagId, tag);
            }
        }

        if (post != null) {
            post.setComments(new ArrayList<>(comments.values()));
            post.setTags(new HashSet<>(tags.values()));
        }

        return post;
    }
}
