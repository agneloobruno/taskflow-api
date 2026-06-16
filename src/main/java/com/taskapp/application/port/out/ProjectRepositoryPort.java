package com.taskapp.application.port.out;

import com.taskapp.domain.entity.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepositoryPort {
    Project save(Project project);
    Optional<Project> findById(UUID id);
    List<Project> findByOwnerId(UUID ownerId);
    void deleteById(UUID id);
}
