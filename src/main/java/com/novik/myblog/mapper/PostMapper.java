package com.novik.myblog.mapper;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.model.Post;
import com.novik.myblog.service.TagService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostMapper {

    private final TagService tagService;

    public Post toModel(NewPostDto dto) {
        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .tags(tagService.getTags(dto.getTags()))
                .build();
    }
}
