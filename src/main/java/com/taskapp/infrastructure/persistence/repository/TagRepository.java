package com.taskapp.infrastructure.persistence.repository;

import com.taskapp.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    List<Tag> findByProjectId(UUID projectId);
}
