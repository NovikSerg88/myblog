package com.novik.myblog.service;

import com.novik.myblog.config.TestConfig;
import com.novik.myblog.dto.CommentDto;
import com.novik.myblog.dto.NewPostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class CommentServiceIntegrationTests {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long testPostId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM comments");
        jdbcTemplate.update("DELETE FROM posts");

        NewPostDto newPostDto = new NewPostDto();
        newPostDto.setTitle("Test Post");
        newPostDto.setContent("Test Content");
        testPostId = postService.save(newPostDto);
    }


    @Test
    void save_shouldSaveComment() {
        CommentDto commentDto = commentService.createComment(testPostId, "comment");
        assertEquals("comment", commentDto.getContent());
    }

    @Test
    void delete_shouldDeleteCommentButDoesNotDeletePost() {
        String commentText = "New Comment";
        CommentDto savedComment = commentService.createComment(testPostId, commentText);
        Long commentId = savedComment.getId();

        Integer countBefore = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM comments WHERE id = ?",
                Integer.class,
                commentId
        );
        assertEquals(1, countBefore);

        commentService.deleteComment(commentId);

        Integer countAfter = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM comments WHERE id = ?",
                Integer.class,
                commentId
        );
        assertEquals(0, countAfter);

        assertNotNull(postService.findById(testPostId));
    }
}
