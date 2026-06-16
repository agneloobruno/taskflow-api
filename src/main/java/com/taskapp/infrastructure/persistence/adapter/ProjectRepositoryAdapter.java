package com.taskapp.infrastructure.persistence.adapter;

import com.taskapp.application.port.out.ProjectRepositoryPort;
import com.taskapp.domain.entity.Project;
import com.taskapp.infrastructure.persistence.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProjectRepositoryAdapter implements ProjectRepositoryPort {

    private final ProjectRepository projectRepository;

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> findByOwnerId(UUID ownerId) {
        return projectRepository.findByOwnerId(ownerId);
    }

    @Override
    public void deleteById(UUID id) {
        projectRepository.deleteById(id);
    }
}
