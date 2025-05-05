package com.novik.myblog.repository;

import com.novik.myblog.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Long save(Post post);

    List<Post> findAll(int limit, int offset);

    List<Post> findByTitle(String tag, int limit, int offset);

    Optional<Post> findById(Long id);

    List<Post> findAllPostsWithTags(int limit, int offset);

    List<Post> findByTagIds(List<Long> Ids, int limit, int offset);

    void deletePostById(Long id);

    int update(Post post);
}
