package com.taskapp.application.port.in;

import com.taskapp.application.dto.request.CreateTaskRequest;
import com.taskapp.application.dto.response.TaskResponse;

import java.util.UUID;

public interface CreateTaskUseCase {
    TaskResponse createTask(CreateTaskRequest request, UUID userId);
}
