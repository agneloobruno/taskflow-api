package com.taskapp.application.dto.request;

import com.taskapp.domain.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateTaskRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be at most 200 characters")
    String title,

    String description,

    Priority priority,

    LocalDate dueDate,

    @NotNull(message = "Project ID is required")
    UUID projectId,

    UUID assigneeId,

    List<UUID> tagIds
) {}
