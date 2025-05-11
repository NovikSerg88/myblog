package com.novik.myblog.repository;

import com.novik.myblog.model.Tag;

import java.util.Optional;

public interface TagRepository {

    Tag save(Tag tag);

    Optional<Tag> findTagByTitle(String title);

    void savePostTags(Long postId, Long tagId);

    void deleteAllRelationsByPostId(Long postId);
}

