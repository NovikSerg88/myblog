package com.novik.myblog.controller;

import com.novik.myblog.dto.NewPostDto;
import com.novik.myblog.dto.PostDto;
import com.novik.myblog.dto.PostPreviewDto;

import com.novik.myblog.mapper.PostMapper;
import com.novik.myblog.model.Post;
import com.novik.myblog.service.PostService;
import com.novik.myblog.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final TagService tagService;
    private final PostMapper postMapper;

    @PostMapping
    public String save(@Valid @ModelAttribute NewPostDto newPostDto) {
        log.info("Save a new post: {}", newPostDto);
        postService.save(newPostDto);
        return "redirect:/posts";
    }

    @GetMapping()
    public String getPosts(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        List<Post> posts;
        if (tag != null && !tag.isEmpty()) {
            posts = tagService.findTagByTitle(tag)
                    .map(t -> postService.findByTagId(t.getId(), page, size))
                    .orElseGet(Collections::emptyList);
        } else {
            posts = postService.findAll(page, size);
        }

        List<PostPreviewDto> dtos = posts.stream().map(postMapper::toPreviewDto).toList();

        model.addAttribute("posts", dtos);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);

        if (tag != null)
            model.addAttribute("tag", tag);

        return "posts";
    }

    @GetMapping("/add")
    public String showAddPostForm(Model model) {
        model.addAttribute("post", null);
        return "add-post";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {

        PostDto postDto = postService.findById(id);
        if (postDto == null) {
            return "error";
        }

        model.addAttribute("post", postDto);
        model.addAttribute("comments", postDto.getComments());

        return "post";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePostWithRelations(id);
        return "redirect:/posts";
    }
}
