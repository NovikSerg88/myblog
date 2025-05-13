package com.novik.myblog.mapper;

import com.novik.myblog.config.TestConfig;
import com.novik.myblog.dto.CommentDto;
import com.novik.myblog.model.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class CommentMapperTests {

    @Test
    public void commentToDto_shouldMapAllFieldsCorrectly() {
        Comment comment = new Comment(1L,"test content", 1L);

        CommentDto dto = new CommentMapper().commentToDto(comment);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("test content", dto.getContent());
    }
}
