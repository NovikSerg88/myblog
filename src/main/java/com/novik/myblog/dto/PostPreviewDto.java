package com.novik.myblog.dto;

import com.novik.myblog.model.Comment;
import com.novik.myblog.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostPreviewDto {

    public Long id;

    public String title;

    public String previewText;

    public String imageUrl;

    public int likesCount;

    public int commentsCount;

    public Set<String> tags;

}
