package com.novik.myblog.repository;

import com.novik.myblog.model.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagRepository {

    Tag save(Tag tag);

    Optional<Tag> findTagByTitle(String title);

    Optional<Long> findTagIdByTitle(String title);

    void savePostTags(Long postId, Long tagId);

    void deleteAllRelationsByPostId(Long postId);
}

