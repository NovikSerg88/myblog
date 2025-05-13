package com.novik.myblog.service;

import com.novik.myblog.config.TestConfig;
import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.dto.PostDto;
import com.novik.myblog.exception.NotFoundException;
import com.novik.myblog.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class PostServiceIntegrationTests {

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM post_tags");
        jdbcTemplate.update("DELETE FROM comments");
        jdbcTemplate.update("DELETE FROM posts");
        jdbcTemplate.update("DELETE FROM tags");
    }

    @Test
    void save_shouldSavePostAndTags() {
        NewPostDto newPostDto = new NewPostDto();
        newPostDto.setTitle("Test Post");
        newPostDto.setContent("This is a test content for the post");
        newPostDto.setTags("java,spring");

        Long postId = postService.save(newPostDto);

        assertNotNull(postId);

        PostDto savedPost = postService.findById(postId);
        assertEquals("Test Post", savedPost.getTitle());
        assertEquals("java, spring", savedPost.getTags());
    }

    @Test
    void edit_shouldUpdatePostAndTags() {
        Long postId = createTestPost();

        NewPostDto updatedPost = new NewPostDto();
        updatedPost.setTitle("Updated Title");
        updatedPost.setContent("Updated content");
        updatedPost.setTags("updated,tag");

        int updateCount = postService.edit(postId, updatedPost);

        assertEquals(1, updateCount);

        PostDto result = postService.findById(postId);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("tag, updated", result.getTags());
    }

    @Test
    void findById_shouldReturnPost() {
        Long postId = createTestPost();

        PostDto result = postService.findById(postId);

        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
    }

    @Test
    void findById_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            postService.findById(999L);
        });
    }

    @Test
    void findAll_shouldReturnPaginatedPosts() {
        createTestPost();
        createTestPost();

        List<Post> result = postService.findAll(0, 10);

        assertEquals(2, result.size());
    }

    @Test
    void findByTagId_shouldReturnPostsWithTag() {
        Long postId = createTestPost();
        Long tagId = jdbcTemplate.queryForObject(
                "SELECT id FROM tags WHERE title = 'java'", Long.class);

        List<Post> result = postService.findByTagId(tagId, 0, 10);

        assertEquals(1, result.size());
        assertEquals(postId, result.get(0).getId());
    }

    @Test
    void deletePostWithRelations_shouldDeletePostAndRelations() {
        Long postId = createTestPost();

        postService.deletePostWithRelations(postId);

        assertThrows(NotFoundException.class, () -> {
            postService.findById(postId);
        });

        int tagCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post_tags WHERE post_id = ?",
                Integer.class, postId);
        assertEquals(0, tagCount);
    }

    @Test
    void likePost_shouldIncrementLikesCount() {
        Long postId = createTestPost();

        Post result = postService.likePost(postId);

        assertEquals(1, result.getLikesCount());

        PostDto updatedPost = postService.findById(postId);
        assertEquals(1, updatedPost.getLikeCount());
    }

    private Long createTestPost() {
        NewPostDto newPostDto = new NewPostDto();
        newPostDto.setTitle("Test Post");
        newPostDto.setContent("Test content");
        newPostDto.setTags("java,test");
        return postService.save(newPostDto);
    }
}
