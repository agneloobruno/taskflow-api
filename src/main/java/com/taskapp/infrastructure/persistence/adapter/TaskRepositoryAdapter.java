package com.taskapp.infrastructure.persistence.adapter;

import com.taskapp.application.port.out.TaskRepositoryPort;
import com.taskapp.domain.entity.Task;
import com.taskapp.domain.enums.Priority;
import com.taskapp.domain.enums.TaskStatus;
import com.taskapp.infrastructure.persistence.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findByProjectId(UUID projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Override
    public List<Task> findFiltered(UUID projectId, TaskStatus status, Priority priority, String search) {
        return taskRepository.findFiltered(projectId, status, priority, search);
    }

    @Override
    public void deleteById(UUID id) {
        taskRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return taskRepository.existsById(id);
    }
}
