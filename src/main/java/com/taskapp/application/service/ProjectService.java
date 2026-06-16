package com.taskapp.application.service;

import com.taskapp.application.dto.request.CreateProjectRequest;
import com.taskapp.application.dto.response.ProjectResponse;
import com.taskapp.application.port.in.CreateProjectUseCase;
import com.taskapp.application.port.out.ProjectRepositoryPort;
import com.taskapp.application.port.out.UserRepositoryPort;
import com.taskapp.domain.entity.Project;
import com.taskapp.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService implements CreateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final UserRepositoryPort userRepository;

    @Override
    public ProjectResponse createProject(CreateProjectRequest request, UUID userId) {
        User owner = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        Project project = Project.builder()
            .name(request.name())
            .description(request.description())
            .owner(owner)
            .build();

        return ProjectResponse.from(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getUserProjects(UUID userId) {
        return projectRepository.findByOwnerId(userId).stream()
            .map(ProjectResponse::from)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));

        if (!project.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to this project");
        }

        return ProjectResponse.from(project);
    }

    @Override
    public void deleteProject(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NoSuchElementException("Project not found: " + projectId));

        if (!project.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to this project");
        }

        projectRepository.deleteById(projectId);
    }
}
