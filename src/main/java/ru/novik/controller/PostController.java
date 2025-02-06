package ru.novik.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import ru.novik.model.Post;
import ru.novik.model.PostPreview;
import ru.novik.service.PostService;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public String getPosts(@RequestParam(name = "filter", required = false) String filterTag,
                           @RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "size", defaultValue = "10") int size,
                           Model model) {

        List<PostPreview> postPreviews = postService.getPostPreviews(page, size, filterTag);
        int totalPosts = StringUtils.isEmpty(filterTag)
                ? postService.getCount()
                : postService.getCountByTag(filterTag);

        int totalPages = (totalPosts / size) + (totalPosts % size > 0 ? 1 : 0);
        model.addAttribute("posts", postPreviews);
        model.addAttribute("currentPage", page);
        model.addAttribute("sizePage", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("filterTag", filterTag);

        return "posts";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable long id, Model model) {
        // Логика для отображения формы добавления поста
        return "post";
    }

    @GetMapping("/add")
    public String showAddPostForm(Model model) {
        return "add-post";
    }

    @PostMapping
    public String addPost(@ModelAttribute Post post) {
        // Логика для сохранения нового поста
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String updatePost(@PathVariable long id, @ModelAttribute Post post) {
        return "redirect:/posts/" + id;
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable long id) {
        return "redirect:/posts";
    }

}
