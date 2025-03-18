package com.novik.myblog.controller;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public String save(@ModelAttribute NewPostDto newPostDto) {
        log.info("Create new post: {}", newPostDto);
        postService.save(newPostDto);

        return "redirect:/post";
    }
}
