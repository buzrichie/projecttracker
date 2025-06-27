package org.amalitechrichmond.projecttracker.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
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

