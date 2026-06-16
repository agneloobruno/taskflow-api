package com.taskapp.infrastructure.persistence.repository;

import com.taskapp.domain.entity.Task;
import com.taskapp.domain.enums.Priority;
import com.taskapp.domain.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByProjectId(UUID projectId);

    @Query("""
        SELECT t FROM Task t
        WHERE t.project.id = :projectId
          AND (:status IS NULL OR t.status = :status)
          AND (:priority IS NULL OR t.priority = :priority)
          AND (:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))
        ORDER BY t.createdAt DESC
        """)
    List<Task> findFiltered(
        @Param("projectId") UUID projectId,
        @Param("status") TaskStatus status,
        @Param("priority") Priority priority,
        @Param("search") String search
    );
}
