package com.example.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Request body for creating or updating a post")
public class PostRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be under 255 characters")
    @Schema(description = "Title of the post", example = "My First Post")
    private String title;

    @NotBlank(message = "Content is required")
    @Schema(description = "Full content of the post", example = "Hello from Spring Boot!")
    private String content;

    @NotBlank(message = "Author is required")
    @Schema(description = "Author's name", example = "Alice")
    private String author;

    @Schema(description = "Whether the post is publicly visible", example = "false")
    private boolean published = false;
}