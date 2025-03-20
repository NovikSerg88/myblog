package com.novik.myblog.service;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.mapper.PostMapper;
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
    private final PostMapper postMapper;

    @Override
    public Post save(NewPostDto newPostDto) {
        Post post = postMapper.toModel(newPostDto);
        return postRepository.save(post);
    }
}
