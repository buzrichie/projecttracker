package org.amalitechrichmond.projecttracker.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class DeveloperDTO {
    private Long id;
    private String name;
    private String email;
    private List<String> skills;
}

