package com.novik.myblog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDto {
    public Long id;

    public String title;

    public String content;

    public String imageUrl;

    public int likeCount;

    public List<CommentDto> comments;

    public String tags;
}
