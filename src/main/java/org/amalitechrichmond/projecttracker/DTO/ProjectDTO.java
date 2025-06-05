package org.amalitechrichmond.projecttracker.DTO;

import lombok.Getter;
import lombok.Setter;
import org.amalitechrichmond.projecttracker.enums.ProjectStatus;

import java.time.LocalDate;

@Setter
@Getter
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate deadline;
    private ProjectStatus status;
}

