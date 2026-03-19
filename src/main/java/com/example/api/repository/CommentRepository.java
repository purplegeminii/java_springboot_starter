package com.example.api.repository;

import com.example.api.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // All comments for a given post, newest first
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);

    // Count comments on a post — useful for summary views
    long countByPostId(Long postId);
}