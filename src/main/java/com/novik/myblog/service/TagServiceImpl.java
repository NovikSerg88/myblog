package com.novik.myblog.service;

import com.novik.myblog.model.Tag;

import com.novik.myblog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Set<Tag> getTags(String tagsString) {
        if (tagsString == null || tagsString.trim().isEmpty()) {
            return Collections.emptySet();
        }

        return Arrays.stream(tagsString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .map(this::findOrCreateTag)
                .collect(Collectors.toSet());
    }

    @Override
    public void addPostTags(Long postId) {

    }

    @Override
    public void savePostTags(Long postId, Long tagId) {
        tagRepository.savePostTags(postId, tagId);
    }

    private Tag findOrCreateTag(String tagTitle) {
        return tagRepository.findByTitle(tagTitle)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setTitle(tagTitle);
                    return tagRepository.save(newTag);
                });
    }
}
