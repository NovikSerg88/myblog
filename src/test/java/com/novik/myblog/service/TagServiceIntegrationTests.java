package com.novik.myblog.service;

import com.novik.myblog.config.TestConfig;
import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.dto.PostDto;
import com.novik.myblog.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TagServiceIntegrationTests {

    @Autowired
    private TagServiceImpl tagService;

    @Autowired
    private PostService postService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM post_tags");
        jdbcTemplate.update("DELETE FROM tags");
    }

    @Test
    void getTags_shouldCreateNewTagsWhenNotExist() {
        Set<Tag> tags = tagService.getTags("java,spring,test");

        assertEquals(3, tags.size());
        assertTrue(tags.stream().anyMatch(t -> t.getTitle().equals("java")));
        assertTrue(tags.stream().anyMatch(t -> t.getTitle().equals("spring")));
        assertTrue(tags.stream().anyMatch(t -> t.getTitle().equals("test")));
    }

    @Test
    void getTags_shouldReturnExistingTags() {
        tagService.getTags("java,spring");

        Set<Tag> tags = tagService.getTags("java,spring,test");

        assertEquals(3, tags.size());

        long tagsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tags", Long.class);
        assertEquals(3, tagsCount);
    }

    @Test
    void getTags_shouldConvertToLowerCase() {
        Set<Tag> tags = tagService.getTags("JAVA,Spring");

        assertTrue(tags.stream().anyMatch(t -> t.getTitle().equals("java")));
        assertTrue(tags.stream().anyMatch(t -> t.getTitle().equals("spring")));
    }

    @Test
    void getTags_shouldReturnEmptySetForNullOrEmptyInput() {
        assertTrue(tagService.getTags(null).isEmpty());
        assertTrue(tagService.getTags("").isEmpty());
        assertTrue(tagService.getTags("   ").isEmpty());
    }

    @Test
    void savePostTags_shouldCreateRelationBetweenPostAndTag() {
        NewPostDto newPostDto = new NewPostDto();
        newPostDto.setTitle("Test Post");
        newPostDto.setContent("Test Content");
        newPostDto.setTags("");

        Long postId = postService.save(newPostDto);
        Set<Tag> tags = tagService.getTags("tag1,tag2");

        tags.forEach(tag -> tagService.savePostTags(postId, tag.getId()));

        PostDto postDto = postService.findById(postId);

        assertEquals("tag2, tag1", postDto.getTags());
    }


    @Test
    void findTagByTitle_shouldReturnExistingTag() {
        createTestTag("java");

        Optional<Tag> tag = tagService.findTagByTitle("java");

        assertTrue(tag.isPresent());
        assertEquals("java", tag.get().getTitle());
    }

    @Test
    void findTagByTitle_shouldReturnEmptyForNonExistingTag() {
        Optional<Tag> tag = tagService.findTagByTitle("non-existing");

        assertFalse(tag.isPresent());
    }

    @Test
    void findTagByTitle_shouldBeCaseInsensitive() {
        createTestTag("java");

        assertFalse(tagService.findTagByTitle("JAVA").isPresent());
        assertTrue(tagService.findTagByTitle("java").isPresent());
    }

    @Test
    void deletePostTags_shouldRemoveAllRelationsForPost() {
        NewPostDto newPostDto = new NewPostDto();
        newPostDto.setTitle("Test Post");
        newPostDto.setContent("Test Content");
        newPostDto.setTags("");

        Long postId = postService.save(newPostDto);
        Set<Tag> tags = tagService.getTags("tag1,tag2");

        tags.forEach(tag -> tagService.savePostTags(postId, tag.getId()));

        tagService.deletePostTags(postId);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post_tags WHERE post_id = ?",
                Integer.class,
                postId
        );

        assertEquals(0, count, "Все связи для поста должны быть удалены");
    }

    private Long createTestTag(String title) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO tags (title) VALUES (?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, title.toLowerCase());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        assertNotNull(key, "Failed to obtain generated key");
        return key.longValue();
    }
}
