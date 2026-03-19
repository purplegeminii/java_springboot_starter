package com.example.api.service;

import com.example.api.dto.PostRequest;
import com.example.api.dto.PostResponse;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.model.Post;
import com.example.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return PostResponse.from(post);
    }

    public List<PostResponse> getPublishedPosts() {
        return postRepository.findByPublished(true)
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public List<PostResponse> searchPosts(String keyword) {
        return postRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse createPost(PostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(request.getAuthor());
        post.setPublished(request.isPublished());
        return PostResponse.from(postRepository.save(post));
    }

    public PostResponse updatePost(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(request.getAuthor());
        post.setPublished(request.isPublished());
        return PostResponse.from(postRepository.save(post));
    }

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }
}