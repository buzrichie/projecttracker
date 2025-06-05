package org.amalitechrichmond.projecttracker.DTO;

import lombok.Getter;
import lombok.Setter;
import org.amalitechrichmond.projecttracker.enums.TaskStatus;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
    private Long projectId;
    private Set<Long> developerIds;
}

