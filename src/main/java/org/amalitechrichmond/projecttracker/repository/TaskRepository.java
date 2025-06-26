package org.amalitechrichmond.projecttracker.repository;

import org.amalitechrichmond.projecttracker.model.Developer;
import org.amalitechrichmond.projecttracker.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    Page<Task> findByDevelopers_Id(Long developerId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.dueDate < CURRENT_DATE AND t.status <> 'COMPLETED'")
    List<Task> findOverdueTasks();

    @Query("SELECT d FROM Developer d LEFT JOIN d.tasks t GROUP BY d ORDER BY COUNT(t) DESC")
    Page<Developer> findTopDevelopers(Pageable pageable);

    @Query("SELECT t.status AS status, COUNT(t) AS count FROM Task t GROUP BY t.status")
    List<TaskStatusCount> countTasksByStatus();

    @Query("SELECT t FROM Task t JOIN t.developers d WHERE d.id = :userId")
    List<Task> findTasksByUserId(Long userId);

}
