package com.novik.myblog.repository;

import com.novik.myblog.model.Tag;

import java.util.Set;

public interface TagRepository {

    Tag save(Tag tag);

    Set<Tag> findTagsByTitleIn(Set<String> titles);
}
