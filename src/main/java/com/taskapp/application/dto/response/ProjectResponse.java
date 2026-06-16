package com.taskapp.application.dto.response;

import com.taskapp.domain.entity.Project;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponse(
    UUID id,
    String name,
    String description,
    UserResponse owner,
    int taskCount,
    LocalDateTime createdAt
) {
    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            UserResponse.from(project.getOwner()),
            project.getTasks() != null ? project.getTasks().size() : 0,
            project.getCreatedAt()
        );
    }
}
