package com.novik.myblog.mapper;

import com.novik.myblog.dto.CommentDto;
import com.novik.myblog.model.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class CommentMapper {

    public CommentDto commentToDto(Comment comment) {
        if (comment == null) {
            log.warn("Attempt to map null comment");
            return null;
        }

        try {
            return CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getText())
                    .build();
        } catch (Exception e) {
            log.error("Error mapping comment: {}", e.getMessage());
            return null;
        }
    }
}
