package com.novik.myblog.repository;

import com.novik.myblog.model.Post;

public interface PostRepository {

    Post save(Post post);
}
