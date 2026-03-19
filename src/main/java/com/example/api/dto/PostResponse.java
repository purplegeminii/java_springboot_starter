package com.example.api.dto;

import com.example.api.model.Post;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int commentCount;

    public static PostResponse from(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setAuthor(post.getAuthor());
        response.setPublished(post.isPublished());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setCommentCount(post.getComments().size());
        return response;
    }
}