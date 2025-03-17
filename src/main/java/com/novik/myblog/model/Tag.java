package com.novik.myblog.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Tag {

    private Long id;

    @NonNull
    private String title;
}
