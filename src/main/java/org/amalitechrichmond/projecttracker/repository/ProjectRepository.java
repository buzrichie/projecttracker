package org.amalitechrichmond.projecttracker.repository;

import org.amalitechrichmond.projecttracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE p.tasks IS EMPTY")
    List<Project> findProjectsWithoutTasks();

}
