package org.amalitechrichmond.projecttracker.DTO;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Setter
@Getter
public class DeveloperDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotEmpty(message = "At least one skill is required")
    private List<@Size(min = 2, message = "Each skill must have at least 2 characters") String> skills;

    private Set<Long> taskIds;
}


