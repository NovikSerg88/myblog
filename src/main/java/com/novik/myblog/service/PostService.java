package com.novik.myblog.service;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.model.Post;

public interface PostService {

    Post save(NewPostDto newPostDto);
}
