package com.novik.myblog.model;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Post {

    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String content;

    private String imageUrl;

    private List<Comment> comments = new ArrayList<>();

    private Set<Tag> tags = new HashSet<>();

    private int likesCount;

    private int commentsCount;
}
