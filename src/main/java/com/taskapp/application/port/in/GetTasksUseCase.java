package com.taskapp.application.port.in;

import com.taskapp.application.dto.response.TaskResponse;
import com.taskapp.domain.enums.Priority;
import com.taskapp.domain.enums.TaskStatus;

import java.util.List;
import java.util.UUID;

public interface GetTasksUseCase {
    List<TaskResponse> getTasksByProject(UUID projectId, UUID userId);
    TaskResponse getTaskById(UUID taskId, UUID userId);
    List<TaskResponse> getFilteredTasks(UUID projectId, TaskStatus status, Priority priority, String search, UUID userId);
}
