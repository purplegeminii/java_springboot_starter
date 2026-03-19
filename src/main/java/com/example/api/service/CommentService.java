package com.example.api.service;

import com.example.api.dto.CommentRequest;
import com.example.api.dto.CommentResponse;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.model.Comment;
import com.example.api.model.Post;
import com.example.api.repository.CommentRepository;
import com.example.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<CommentResponse> getCommentsForPost(Long postId) {
        // Verify the post exists first — return 404 if not
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    public CommentResponse addComment(Long postId, CommentRequest request) {
        // Fetch the parent post — throws 404 if missing
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(request.getAuthor());
        comment.setPost(post);          // ← wire the relationship

        return CommentResponse.from(commentRepository.save(comment));
    }

    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        // Make sure this comment actually belongs to the post in the URL
        if (!comment.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("Comment not found for this post");
        }

        commentRepository.delete(comment);
    }
}