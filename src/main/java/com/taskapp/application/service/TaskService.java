package com.taskapp.application.service;

import com.taskapp.application.dto.request.CreateTaskRequest;
import com.taskapp.application.dto.request.UpdateTaskRequest;
import com.taskapp.application.dto.response.TaskResponse;
import com.taskapp.application.port.in.CreateTaskUseCase;
import com.taskapp.application.port.in.DeleteTaskUseCase;
import com.taskapp.application.port.in.GetTasksUseCase;
import com.taskapp.application.port.in.UpdateTaskUseCase;
import com.taskapp.application.port.out.ProjectRepositoryPort;
import com.taskapp.application.port.out.TaskRepositoryPort;
import com.taskapp.application.port.out.UserRepositoryPort;
import com.taskapp.domain.entity.Project;
import com.taskapp.domain.entity.Tag;
import com.taskapp.domain.entity.Task;
import com.taskapp.domain.entity.User;
import com.taskapp.domain.enums.Priority;
import com.taskapp.domain.enums.TaskStatus;
import com.taskapp.infrastructure.persistence.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService implements CreateTaskUseCase, UpdateTaskUseCase, DeleteTaskUseCase, GetTasksUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;
    private final UserRepositoryPort userRepository;
    private final TagRepository tagRepository;

    @Override
    public TaskResponse createTask(CreateTaskRequest request, UUID userId) {
        User creator = findUser(userId);

        Project project = projectRepository.findById(request.projectId())
            .orElseThrow(() -> new NoSuchElementException("Project not found: " + request.projectId()));

        if (!project.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to this project");
        }

        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = findUser(request.assigneeId());
        }

        Set<Tag> tags = new HashSet<>();
        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
            tags = new HashSet<>(tagRepository.findAllById(request.tagIds()));
        }

        Task task = Task.builder()
            .title(request.title())
            .description(request.description())
            .priority(request.priority() != null ? request.priority() : Priority.MEDIUM)
            .dueDate(request.dueDate())
            .project(project)
            .createdBy(creator)
            .assignee(assignee)
            .tags(tags)
            .build();

        return TaskResponse.from(taskRepository.save(task));
    }

    @Override
    public TaskResponse updateTask(UUID taskId, UpdateTaskRequest request, UUID userId) {
        Task task = findTask(taskId);
        assertProjectAccess(task.getProject(), userId);

        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.status() != null) task.setStatus(request.status());
        if (request.priority() != null) task.setPriority(request.priority());
        if (request.dueDate() != null) task.setDueDate(request.dueDate());
        if (request.assigneeId() != null) task.setAssignee(findUser(request.assigneeId()));

        return TaskResponse.from(taskRepository.save(task));
    }

    @Override
    public void deleteTask(UUID taskId, UUID userId) {
        Task task = findTask(taskId);
        assertProjectAccess(task.getProject(), userId);
        taskRepository.deleteById(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByProject(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));
        assertProjectAccess(project, userId);
        return taskRepository.findByProjectId(projectId).stream()
            .map(TaskResponse::from)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID taskId, UUID userId) {
        Task task = findTask(taskId);
        assertProjectAccess(task.getProject(), userId);
        return TaskResponse.from(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getFilteredTasks(UUID projectId, TaskStatus status, Priority priority, String search, UUID userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));
        assertProjectAccess(project, userId);
        return taskRepository.findFiltered(projectId, status, priority, search).stream()
            .map(TaskResponse::from)
            .toList();
    }

    private Task findTask(UUID taskId) {
        return taskRepository.findById(taskId)
            .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
    }

    private void assertProjectAccess(Project project, UUID userId) {
        if (!project.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
    }
}
