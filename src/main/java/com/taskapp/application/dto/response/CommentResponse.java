package com.taskapp.application.dto.response;

import com.taskapp.domain.entity.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
    UUID id,
    String content,
    UserResponse author,
    LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getContent(),
            UserResponse.from(comment.getAuthor()),
            comment.getCreatedAt()
        );
    }
}
