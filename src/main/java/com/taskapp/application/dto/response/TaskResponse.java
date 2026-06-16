package com.taskapp.application.dto.response;

import com.taskapp.domain.entity.Task;
import com.taskapp.domain.enums.Priority;
import com.taskapp.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskResponse(
    UUID id,
    String title,
    String description,
    TaskStatus status,
    Priority priority,
    LocalDate dueDate,
    UUID projectId,
    String projectName,
    UserResponse createdBy,
    UserResponse assignee,
    List<TagResponse> tags,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getDueDate(),
            task.getProject() != null ? task.getProject().getId() : null,
            task.getProject() != null ? task.getProject().getName() : null,
            UserResponse.from(task.getCreatedBy()),
            UserResponse.from(task.getAssignee()),
            task.getTags().stream().map(TagResponse::from).toList(),
            task.getCreatedAt(),
            task.getUpdatedAt()
        );
    }
}
