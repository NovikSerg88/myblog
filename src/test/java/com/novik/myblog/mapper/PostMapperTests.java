package com.novik.myblog.mapper;

import com.novik.myblog.config.TestConfig;
import com.novik.myblog.dto.*;
import com.novik.myblog.model.*;
import com.novik.myblog.model.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, PostMapperTests.MockConfig.class})
public class PostMapperTests {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Configuration
    public static class MockConfig {
        @Bean
        @Primary
        public CommentMapper commentMapper() {
            return mock(CommentMapper.class);
        }
    }

    @Test
    void toModel_shouldMapBasicFields() {
        NewPostDto dto = NewPostDto.builder()
                .title("Test Title")
                .content("Test Content")
                .imageUrl("test.jpg")
                .build();

        Post post = postMapper.toModel(dto);

        assertEquals(dto.getTitle(), post.getTitle());
        assertEquals(dto.getContent(), post.getContent());
        assertEquals(dto.getImageUrl(), post.getImageUrl());
        assertNull(post.getId());
        assertNull(post.getTags());
    }

    @Test
    void toPreviewDto_shouldMapAllFields() {
        Post post = Post.builder()
                .id(1L)
                .title("Test Post")
                .content("Full content of the test post")
                .imageUrl("test.jpg")
                .likesCount(10)
                .commentsCount(5)
                .tags(Set.of(new Tag(1L, "tag1"), new Tag(2L, "tag2")))
                .build();

        PostPreviewDto dto = postMapper.toPreviewDto(post);

        assertEquals(post.getId(), dto.getId());
        assertEquals(post.getTitle(), dto.getTitle());
        assertTrue(dto.getPreviewText().contains("Full content"));
        assertEquals(post.getImageUrl(), dto.getImageUrl());
        assertEquals(post.getLikesCount(), dto.getLikesCount());
        assertEquals(post.getCommentsCount(), dto.getCommentsCount());
        assertEquals(2, dto.getTags().size());
        assertTrue(dto.getTags().containsAll(Set.of("tag1", "tag2")));
    }

    @Test
    void toPreviewDto_shouldHandleNullTags() {
        Post post = Post.builder()
                .id(1L)
                .title("Test")
                .content("Content")
                .tags(null)
                .build();

        PostPreviewDto dto = postMapper.toPreviewDto(post);

        assertNotNull(dto.getTags());
        assertTrue(dto.getTags().isEmpty());
    }

    @Test
    void toDto_shouldMapAllFieldsWithComments() {
        Post post = Post.builder()
                .id(1L)
                .title("Main Post")
                .content("Post content")
                .imageUrl("post.jpg")
                .likesCount(15)
                .tags(Set.of(new Tag(1L, "tech"), new Tag(2L, "java")))
                .comments(List.of(
                        new Comment(1L, "First comment", 1L),
                        new Comment(2L, "Second comment", 1L)
                ))
                .build();

        List<Comment> commentsList = new ArrayList<>(post.getComments());
        when(commentMapper.commentToDto(commentsList.get(0)))
                .thenReturn(new CommentDto(1L, "First comment"));
        when(commentMapper.commentToDto(commentsList.get(1)))
                .thenReturn(new CommentDto(2L, "Second comment"));

        PostDto dto = postMapper.toDto(post);

        assertEquals(post.getId(), dto.getId());
        assertEquals(post.getTitle(), dto.getTitle());
        assertEquals(post.getContent(), dto.getContent());
        assertEquals(post.getImageUrl(), dto.getImageUrl());
        assertEquals(post.getLikesCount(), dto.getLikeCount());
        assertEquals(2, dto.getComments().size());
    }

    @Test
    void toDto_shouldHandleEmptyComments() {
        Post post = Post.builder()
                .id(1L)
                .title("Empty Comments Post")
                .content("Content")
                .comments(List.of())
                .build();

        PostDto dto = postMapper.toDto(post);

        assertNotNull(dto.getComments());
        assertTrue(dto.getComments().isEmpty());
    }
}