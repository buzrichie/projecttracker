package org.amalitechrichmond.projecttracker.DTO;

import lombok.Data;
import java.util.List;

@Data
public class ProjectSummaryDTO {
    private Long projectId;
    private String projectName;
    private String description;
    private String status;
    private String managerName;
    private List<String> developerNames;
    private int totalTasks;
    private int completedTasks;
}

