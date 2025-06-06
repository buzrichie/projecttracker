package org.amalitechrichmond.projecttracker.repository;

import org.amalitechrichmond.projecttracker.model.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    @Query("SELECT d FROM Developer d LEFT JOIN d.tasks t GROUP BY d ORDER BY COUNT(t) DESC")
    List<Developer> findTop5ByOrderByTasksSizeDesc();

}
