package org.amalitechrichmond.projecttracker.repository;

import org.amalitechrichmond.projecttracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
