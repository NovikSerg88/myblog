package com.novik.myblog.repository;

import com.novik.myblog.model.Post;

import java.util.List;

public interface PostRepository {

    Long save(Post post);

    List<Post> findAll(int limit, int offset);

    List<Post> findByTitle(String tag, int limit, int offset);

    Post findById(String id);

    List<Post> findAllPostsWithTags(int limit, int offset);

    List<Post> findByTagIds(List<Long> Ids, int limit, int offset);
}
