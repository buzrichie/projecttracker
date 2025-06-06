package org.amalitechrichmond.projecttracker.DTO;

import lombok.Getter;
import lombok.Setter;
import org.amalitechrichmond.projecttracker.enums.TaskStatus;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Setter
@Getter

public class TaskDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotBlank(message = "Status is required")
    private TaskStatus status;

    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private Set<Long> developerIds;
}

