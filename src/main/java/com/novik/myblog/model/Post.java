package com.novik.myblog.model;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Post {

    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String content;

    private String imageUrl;

    private List<Comment> comments;

    private Set<Tag> tags;

    private int likesCount;

    private int commentsCount;
}
