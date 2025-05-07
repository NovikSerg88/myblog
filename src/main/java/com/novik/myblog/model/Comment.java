package com.novik.myblog.model;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
public class Comment {

    private Long id;

    @NonNull
    private String text;

    @NonNull
    private Long postId;

}
