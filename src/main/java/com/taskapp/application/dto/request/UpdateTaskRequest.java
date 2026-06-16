package com.taskapp.application.dto.request;

import com.taskapp.domain.enums.Priority;
import com.taskapp.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTaskRequest(
    String title,
    String description,
    TaskStatus status,
    Priority priority,
    LocalDate dueDate,
    UUID assigneeId
) {}
