package com.novik.myblog.service;

import com.novik.myblog.dto.NewPostDto;

import com.novik.myblog.dto.PostDto;
import com.novik.myblog.dto.PostPreviewDto;
import com.novik.myblog.exception.NotFoundException;
import com.novik.myblog.mapper.PostMapper;
import com.novik.myblog.model.Post;

import com.novik.myblog.model.Tag;
import com.novik.myblog.repository.CommentRepository;
import com.novik.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final TagService tagService;
    private final PostMapper postMapper;

    @Override
    public Long save(NewPostDto newPostDto) {
        Post post = postMapper.toModel(newPostDto);
        Long postId = postRepository.save(post);
        Set<Tag> tags = tagService.getTags(newPostDto.getTags());

        tags.forEach(tag -> tagService.savePostTags(postId, tag.getId()));

        return postId;
    }

    @Override
    public int edit(Long id, NewPostDto newPostDto) {
        Post post = postMapper.toModel(newPostDto);
        post.setId(id);
        int updateCount = postRepository.update(post);

        Set<Tag> newTags = tagService.getTags(newPostDto.getTags());

        tagService.deletePostTags(id);

        newTags.forEach(tag -> tagService.savePostTags(id, tag.getId()));

        return updateCount;
    }

    @Override
    public List<PostPreviewDto> getPosts(String tagTitle, int page, int size) {
        List<Post> posts;

//               if (tagTitle != null && !tagTitle.isEmpty()) {
//            posts = postRepository.findByTag(tagTitle, page, size);
//            return List.of(
//                    new PostPreviewDto(1L, "Первый пост", "Содержание первого поста",
//                            "/resources/images/1.jpg", 1, 1, "tag"));
//        } else {
//            posts = postRepository.findAll(page, size);
//        }

        posts = postRepository.findAll(page, size);


        return posts.stream().map(postMapper::toPreviewDto).collect(Collectors.toList());
    }

    @Override
    public PostDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        return postMapper.toDto(post);
    }

    @Override
    public List<Post> findAll(int page, int size) {
        log.info("find all post. Page: {} Size: {}", page, size);
        return postRepository.findAllPostsWithTags(size, page * size);
    }

    @Override
    public List<Post> findByTagId(Long tagId, int page, int size) {
        return postRepository.findByTagIds(List.of(tagId), size, page * size);
    }

    @Override
    public void deletePostWithRelations(Long id) {
        postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        tagService.deletePostTags(id);
        commentService.deletePostComments(id);
        postRepository.deletePostById(id);
    }

    @Override
    public Post likePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException("Post with ID " + postId + " does not exist"));
        post.setLikesCount(post.getLikesCount() + 1);

        postRepository.update(post);

        return post;
    }


}

