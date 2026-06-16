package com.taskapp.presentation.controller;

import com.taskapp.application.dto.request.CreateProjectRequest;
import com.taskapp.application.dto.response.ProjectResponse;
import com.taskapp.application.port.in.CreateProjectUseCase;
import com.taskapp.domain.entity.User;
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
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    private final CreateProjectUseCase projectUseCase;

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ProjectResponse> create(
        @Valid @RequestBody CreateProjectRequest request,
        @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(projectUseCase.createProject(request, currentUser.getId()));
    }

    @GetMapping
    @Operation(summary = "List all projects for the current user")
    public ResponseEntity<List<ProjectResponse>> list(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(projectUseCase.getUserProjects(currentUser.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a project by ID")
    public ResponseEntity<ProjectResponse> getById(
        @PathVariable UUID id,
        @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(projectUseCase.getProjectById(id, currentUser.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project")
    public ResponseEntity<Void> delete(
        @PathVariable UUID id,
        @AuthenticationPrincipal User currentUser
    ) {
        projectUseCase.deleteProject(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
