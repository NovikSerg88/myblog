package com.novik.myblog.service;

import com.novik.myblog.model.Tag;

import java.util.Set;

public interface TagService {

    Set<Tag> getTags(String tags);
}
