package com.novik.myblog.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long id;

    private String text;

    private Long postId;
}
