package com.taskapp.application.port.in;

import com.taskapp.application.dto.request.UpdateTaskRequest;
import com.taskapp.application.dto.response.TaskResponse;

import java.util.UUID;

public interface UpdateTaskUseCase {
    TaskResponse updateTask(UUID taskId, UpdateTaskRequest request, UUID userId);
}
