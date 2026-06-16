package com.taskapp.application.port.in;

import com.taskapp.application.dto.request.CreateProjectRequest;
import com.taskapp.application.dto.response.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface CreateProjectUseCase {
    ProjectResponse createProject(CreateProjectRequest request, UUID userId);
    List<ProjectResponse> getUserProjects(UUID userId);
    ProjectResponse getProjectById(UUID projectId, UUID userId);
    void deleteProject(UUID projectId, UUID userId);
}
