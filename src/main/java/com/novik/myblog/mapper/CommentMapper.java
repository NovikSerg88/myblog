package com.novik.myblog.mapper;

import com.novik.myblog.dto.CommentDto;
import com.novik.myblog.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentMapper {

    public CommentDto commentToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getText())
                .build();
    }
}
