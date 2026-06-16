package com.taskapp.application.port.out;

import com.taskapp.domain.entity.Task;
import com.taskapp.domain.enums.Priority;
import com.taskapp.domain.enums.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {
    Task save(Task task);
    Optional<Task> findById(UUID id);
    List<Task> findByProjectId(UUID projectId);
    List<Task> findFiltered(UUID projectId, TaskStatus status, Priority priority, String search);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
