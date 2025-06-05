package org.amalitechrichmond.projecttracker.repository;

import org.amalitechrichmond.projecttracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
