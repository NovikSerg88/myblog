package ru.novik.service;

import org.thymeleaf.util.StringUtils;
import ru.novik.model.Post;
import ru.novik.model.PostPreview;

import java.util.List;
import java.util.stream.Collectors;

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<PostPreview> getPostPreviews(int page, int size, String tag) {
        List<Post> posts;
        if (StringUtils.isEmpty(tag)) {
            posts = postRepository.findPage(page, size);
        } else {
            posts = postRepository.findPageByTag(page, size, tag);
        }
        return posts.stream()
                .map(this::convertToPostPreview)
                .collect(Collectors.toList());
    }

    private PostPreview convertToPostPreview(Post post) {
        String contentPreview = post.getContent().split("\n")[0];
        if (contentPreview.length() > 100) {
            contentPreview = contentPreview.substring(0, 100) + "...";
        }
        return new PostPreview(
                post.getId(),
                post.getTitle(),
                post.getImageUrl(),
                contentPreview,
                post.getTags(),
                post.getComments().size(),
                post.getLikes()
        );
    }
}
