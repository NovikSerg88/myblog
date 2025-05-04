package com.novik.myblog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {

    public Long id;

    public String content;
}
