package com.taskapp.presentation.controller;

import com.taskapp.application.dto.request.CreateTaskRequest;
import com.taskapp.application.dto.request.UpdateTaskRequest;
import com.taskapp.application.dto.response.TaskResponse;
import com.taskapp.application.port.in.CreateTaskUseCase;
import com.taskapp.application.port.in.DeleteTaskUseCase;
import com.taskapp.application.port.in.GetTasksUseCase;
import com.taskapp.application.port.in.UpdateTaskUseCase;
import com.taskapp.domain.entity.User;
import com.taskapp.domain.enums.Priority;
import com.taskapp.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final GetTasksUseCase getTasksUseCase;

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskResponse> create(
        @Valid @RequestBody CreateTaskRequest request,
        @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(createTaskUseCase.createTask(request, currentUser.getId()));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "List tasks for a project with optional filters")
    public ResponseEntity<List<TaskResponse>> listByProject(
        @PathVariable UUID projectId,
        @RequestParam(required = false) TaskStatus status,
        @RequestParam(required = false) Priority priority,
        @RequestParam(required = false) String search,
        @AuthenticationPrincipal User currentUser
    ) {
        List<TaskResponse> tasks = (status != null || priority != null || search != null)
            ? getTasksUseCase.getFilteredTasks(projectId, status, priority, search, currentUser.getId())
            : getTasksUseCase.getTasksByProject(projectId, currentUser.getId());

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<TaskResponse> getById(
        @PathVariable UUID id,
        @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(getTasksUseCase.getTaskById(id, currentUser.getId()));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a task (partial update)")
    public ResponseEntity<TaskResponse> update(
        @PathVariable UUID id,
        @RequestBody UpdateTaskRequest request,
        @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(updateTaskUseCase.updateTask(id, request, currentUser.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> delete(
        @PathVariable UUID id,
        @AuthenticationPrincipal User currentUser
    ) {
        deleteTaskUseCase.deleteTask(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
