package org.amalitechrichmond.projecttracker.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.amalitechrichmond.projecttracker.enums.ProjectStatus;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Data
@Setter
@Getter
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Project name is required")
    @Size(max = 100, message = "Project name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @FutureOrPresent(message = "Deadline must be today or a future date")
    private LocalDate deadline;

//    @NotBlank(message = "Status is required")
    private ProjectStatus status;
}


