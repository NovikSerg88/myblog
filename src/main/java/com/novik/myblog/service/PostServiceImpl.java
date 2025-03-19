package com.novik.myblog.service;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.model.Post;
import com.novik.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post save(NewPostDto newPostDto) {

        Post post =
        return postRepository.save();
    }
}
