package com.novik.myblog.mapper;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.model.Post;
import com.novik.myblog.model.Tag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PostMapper {

    Post toModel(NewPostDto dto) {
        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .build();
    }
}
