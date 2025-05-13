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
import org.springframework.validation.BindingResult;
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
    public String save(@Valid @ModelAttribute NewPostDto newPostDto,
                       BindingResult bindingResult, Model model) {
        log.info("Save new post: {}", newPostDto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "add-post";
        }

        postService.save(newPostDto);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, @Valid @ModelAttribute NewPostDto newPostDto) {
        log.info("Edit the post with id: {}", id);

        postService.edit(id, newPostDto);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        log.info("Show edit form with id: {}", id);

        PostDto postDto = postService.findById(id);
        model.addAttribute("post", postDto);
        model.addAttribute("editingMode", true);
        return "post";
    }

    @GetMapping()
    public String getPosts(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("Get posts with tag, page, size: {}, {}, {}", tag, page, size);

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
        log.info("Show add post form");

        model.addAttribute("post", null);
        return "add-post";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {
        log.info("Show post with id: {}", id);

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
        log.info("Delete post with id: {}", id);

        postService.deletePostWithRelations(id);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable("id") Long id) {
        log.info("Like post with id: {}", id);

        postService.likePost(id);
        return "redirect:/posts/" + id;
    }
}
