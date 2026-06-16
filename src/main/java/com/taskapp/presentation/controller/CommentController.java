package com.taskapp.presentation.controller;

import com.taskapp.application.dto.request.CreateCommentRequest;
import com.taskapp.application.dto.response.CommentResponse;
import com.taskapp.domain.entity.Comment;
import com.taskapp.domain.entity.Task;
import com.taskapp.domain.entity.User;
import com.taskapp.infrastructure.persistence.repository.CommentRepository;
import com.taskapp.infrastructure.persistence.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Task comment endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "List all comments for a task")
    public ResponseEntity<List<CommentResponse>> list(@PathVariable UUID taskId) {
        return ResponseEntity.ok(
            commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId).stream()
                .map(CommentResponse::from)
                .toList()
        );
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Add a comment to a task")
    public ResponseEntity<CommentResponse> create(
        @PathVariable UUID taskId,
        @Valid @RequestBody CreateCommentRequest request,
        @AuthenticationPrincipal User currentUser
    ) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));

        Comment comment = Comment.builder()
            .content(request.content())
            .task(task)
            .author(currentUser)
            .build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommentResponse.from(commentRepository.saveAndFlush(comment)));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete a comment")
    public ResponseEntity<Void> delete(
        @PathVariable UUID taskId,
        @PathVariable UUID commentId,
        @AuthenticationPrincipal User currentUser
    ) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NoSuchElementException("Comment not found: " + commentId));

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentRepository.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }
}
