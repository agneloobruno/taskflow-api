package com.taskapp.presentation.controller;

import com.taskapp.application.dto.request.TagRequest;
import com.taskapp.application.dto.response.TagResponse;
import com.taskapp.domain.entity.Project;
import com.taskapp.domain.entity.User;
import com.taskapp.infrastructure.persistence.repository.ProjectRepository;
import com.taskapp.infrastructure.persistence.repository.TagRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tags")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags", description = "Tag management per project")
@SecurityRequirement(name = "bearerAuth")
public class TagController {

    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;

    @GetMapping
    @Operation(summary = "List all tags for a project")
    public ResponseEntity<List<TagResponse>> list(
        @PathVariable UUID projectId,
        @AuthenticationPrincipal User currentUser
    ) {
        assertAccess(projectId, currentUser.getId());
        return ResponseEntity.ok(
            tagRepository.findByProjectId(projectId).stream()
                .map(TagResponse::from)
                .toList()
        );
    }

    @PostMapping
    @Operation(summary = "Create a tag in a project")
    public ResponseEntity<TagResponse> create(
        @PathVariable UUID projectId,
        @Valid @RequestBody TagRequest request,
        @AuthenticationPrincipal User currentUser
    ) {
        Project project = assertAccess(projectId, currentUser.getId());

        com.taskapp.domain.entity.Tag tag = com.taskapp.domain.entity.Tag.builder()
            .name(request.name())
            .color(request.color() != null ? request.color() : "#6366f1")
            .project(project)
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(TagResponse.from(tagRepository.save(tag)));
    }

    @DeleteMapping("/{tagId}")
    @Operation(summary = "Delete a tag")
    public ResponseEntity<Void> delete(
        @PathVariable UUID projectId,
        @PathVariable UUID tagId,
        @AuthenticationPrincipal User currentUser
    ) {
        assertAccess(projectId, currentUser.getId());
        tagRepository.deleteById(tagId);
        return ResponseEntity.noContent().build();
    }

    private Project assertAccess(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));
        if (!project.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        return project;
    }
}
