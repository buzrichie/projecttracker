package org.amalitechrichmond.projecttracker.DTO;

import org.amalitechrichmond.projecttracker.enums.ProjectStatus;

import java.time.LocalDate;

public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate deadline;
    private ProjectStatus status;
}

