package com.example.api.controller;

import com.example.api.dto.PostRequest;
import com.example.api.dto.PostResponse;
import com.example.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Create, read, update and delete posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "List all posts", description = "Returns all posts, or filters by title if ?search= is provided")
    public ResponseEntity<List<PostResponse>> getPosts(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(postService.searchPosts(search));
        }
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/published")
    public ResponseEntity<List<PostResponse>> getPublishedPosts() {
        return ResponseEntity.ok(postService.getPublishedPosts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a post by ID")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new post")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing post")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}