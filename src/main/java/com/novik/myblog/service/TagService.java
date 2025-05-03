package com.novik.myblog.service;

import com.novik.myblog.model.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagService {

    Set<Tag> getTags(String tags);

    void addPostTags(Long postId);

    void savePostTags(Long postId, Long tagId);

    Optional<Tag> findTagByTitle(String title);
}
