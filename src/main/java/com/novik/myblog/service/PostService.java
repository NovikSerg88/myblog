package com.novik.myblog.service;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.dto.PostDto;
import com.novik.myblog.dto.PostPreviewDto;
import com.novik.myblog.model.Post;

import java.util.List;

public interface PostService {

    Long save(NewPostDto newPostDto);

    List<PostPreviewDto> getPosts(String tagTitle, int page, int size);

    PostDto findById(Long id);

    List<Post> findAll(int page, int size);
}
