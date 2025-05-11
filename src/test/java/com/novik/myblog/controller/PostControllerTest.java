package com.novik.myblog.controller;

import com.novik.myblog.config.TestConfig;
import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.dto.PostDto;
import com.novik.myblog.dto.PostPreviewDto;
import com.novik.myblog.model.Post;
import com.novik.myblog.service.CommentService;
import com.novik.myblog.service.PostService;
import com.novik.myblog.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PostService postService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void clearDatabase() {
        jdbcTemplate.update("DELETE FROM post_tags");
        jdbcTemplate.update("DELETE FROM comments");
        jdbcTemplate.update("DELETE FROM posts");
        jdbcTemplate.update("DELETE FROM tags");
    }


    @Test
    void savePost_shouldSavePostAndRedirect() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("title", "Test post")
                        .param("content", "Test content")
                        .param("tags", "tag1, tag2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        List<Post> posts = postService.findAll(0, 10);

        Assertions.assertEquals(1, posts.size());

        Assertions.assertEquals("Test post", posts.get(0).getTitle());
        Assertions.assertEquals("Test content", posts.get(0).getContent());
        Assertions.assertNotNull(posts.get(0).getTags());
        Assertions.assertEquals(2, posts.get(0).getTags().size());
        Assertions.assertTrue(posts.get(0).getTags().stream().anyMatch(t -> t.getTitle().equals("tag1")));
        Assertions.assertTrue(posts.get(0).getTags().stream().anyMatch(t -> t.getTitle().equals("tag2")));
    }

    @Test
    void editPost_shouldUpdatePostAndRedirect() throws Exception {
        NewPostDto newPostDto = new NewPostDto("Original title", "Original content", null, "tag1");
        Long postId = postService.save(newPostDto);

        mockMvc.perform(post("/posts/{id}/edit", postId)
                        .param("title", "Updated title")
                        .param("content", "Updated content")
                        .param("tags", "newTag"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId));

        PostDto updatedPost = postService.findById(postId);

        Assertions.assertEquals("Updated title", updatedPost.getTitle());
        Assertions.assertEquals("Updated content", updatedPost.getContent());

        Assertions.assertEquals("newtag", updatedPost.getTags());
    }

    @Test
    void showEditForm_shouldReturnEditViewWithPostData() throws Exception {
        NewPostDto newPostDto = new NewPostDto("Test title", "Test content", null, "tag1");
        Long postId = postService.save(newPostDto);

        MvcResult result = mockMvc.perform(get("/posts/{id}/edit", postId))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("editingMode", true))
                .andReturn();

        PostDto postDto = (PostDto) result.getModelAndView().getModel().get("post");
        Assertions.assertEquals("Test title", postDto.getTitle());
        Assertions.assertEquals("Test content", postDto.getContent());
        Assertions.assertEquals("tag1", postDto.getTags());
    }

    @Test
    void getPosts_withTag_shouldReturnFilteredPosts() throws Exception {
        postService.save(new NewPostDto("Post 1", "Content 1", null, "tag1"));
        postService.save(new NewPostDto("Post 2", "Content 2", null, "tag2"));

        MvcResult result = mockMvc.perform(get("/posts").param("tag", "tag1"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("tag", "tag1"))
                .andReturn();

        List<PostPreviewDto> posts = (List<PostPreviewDto>) result.getModelAndView().getModel().get("posts");
        Assertions.assertTrue(posts.stream().allMatch(p -> p.getTags().contains("tag1")));
    }

    @Test
    void getPost_shouldReturnPostViewWithTagsAsString() throws Exception {
        NewPostDto newPostDto = new NewPostDto("Test title", "Test content", null, "tag1, tag2");
        Long postId = postService.save(newPostDto);

        MvcResult result = mockMvc.perform(get("/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeExists("comments"))
                .andReturn();

        PostDto postDto = (PostDto) result.getModelAndView().getModel().get("post");
        Assertions.assertEquals("Test title", postDto.getTitle());
        Assertions.assertEquals("Test content", postDto.getContent());
        Assertions.assertTrue(postDto.getTags().contains("tag1"));
        Assertions.assertTrue(postDto.getTags().contains("tag2"));
    }
}

