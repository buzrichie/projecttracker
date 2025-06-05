package org.amalitechrichmond.projecttracker.repository;

import org.amalitechrichmond.projecttracker.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}
