package ru.novik.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostPreview {
    private Long id;
    private String title;
    private String imageUrl;
    private String contentPreview;
    private List<String> tags;
    private int commentsCount;
    private int likes;
}
