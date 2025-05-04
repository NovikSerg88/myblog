package com.novik.myblog.mapper;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.dto.PostDto;
import com.novik.myblog.dto.PostPreviewDto;
import com.novik.myblog.model.Post;

import com.novik.myblog.model.Tag;
import com.novik.myblog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PostMapper {

    private final TagService tagService;
    private final CommentMapper commentMapper;

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

    public PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .likeCount(post.getLikesCount())
                .tags(mapTagsToString(post.getTags()))
                .comments(post.getComments().stream()
                        .map(commentMapper::commentToDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private String mapTagsToString(Set<Tag> tags) {
        return tags != null && !tags.isEmpty()
                ? tags.stream()
                .map(Tag::getTitle)
                .filter(title -> title != null && !title.isEmpty())
                .collect(Collectors.joining(", "))
                : null;
    }
}
