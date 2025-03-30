package com.novik.myblog.dto;

import com.novik.myblog.model.Comment;
import com.novik.myblog.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostShortDto {

    private Long id;

    private String title;

    private String content;

    private String imageUrl;

    private int likesCount;

    private List<Comment> comments = new ArrayList<>();

    private Set<Tag> tags = new HashSet<>();

}
