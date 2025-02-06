package ru.novik.service;

import ru.novik.model.PostPreview;

import java.util.List;

public interface PostService {

    List<PostPreview> getPostPreviews(int page, int size, String tag);
}
