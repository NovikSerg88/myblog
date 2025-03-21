package com.novik.myblog.service;

import com.novik.myblog.model.Tag;
import com.novik.myblog.repository.TagRepository;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Set<Tag> getTags(String content) {
        Set<String> titles = Arrays.stream(content.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        Set<Tag> tags = tagRepository.findTagsByTitleIn(titles);

        titles.stream()
                .map(Tag::new)
                .filter(tag -> !tags.contains(tag))
                .forEach(tag -> {
                    tags.add(tagRepository.save(tag));
                });
        return tags;
    }
}
