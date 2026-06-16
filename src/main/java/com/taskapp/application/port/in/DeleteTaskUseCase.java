package com.taskapp.application.port.in;

import java.util.UUID;

public interface DeleteTaskUseCase {
    void deleteTask(UUID taskId, UUID userId);
}
