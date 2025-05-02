package com.novik.myblog.mapper;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.dto.PostPreviewDto;
import com.novik.myblog.model.Post;

import com.novik.myblog.model.Tag;
import com.novik.myblog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PostMapper {

    private final TagService tagService;

    public Post toModel(NewPostDto dto) {
        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .build();
    }

    public PostPreviewDto toPreviewDto(Post post) {
        return PostPreviewDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .previewText(post.getContent())
                .imageUrl(post.getImageUrl())
                .likesCount(post.getLikesCount())
                .commentsCount(post.getCommentsCount())
                .tags(post.getTags().stream().map(Tag::getTitle).collect(Collectors.toSet()))
                .build();
    }
}
