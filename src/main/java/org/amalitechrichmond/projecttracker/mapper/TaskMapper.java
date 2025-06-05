package org.amalitechrichmond.projecttracker.mapper;

import org.amalitechrichmond.projecttracker.DTO.TaskDTO;
import org.amalitechrichmond.projecttracker.model.Developer;
import org.amalitechrichmond.projecttracker.model.Project;
import org.amalitechrichmond.projecttracker.model.Task;

import java.util.Set;
import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());

        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
        }

        Set<Long> devIds = task.getDevelopers().stream()
                .map(Developer::getId)
                .collect(Collectors.toSet());
        dto.setDeveloperIds(devIds);

        return dto;
    }

    public static Task toEntity(TaskDTO dto, Project project, Set<Developer> developers) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());

        task.setProject(project);
        task.setDevelopers(developers);

        return task;
    }
}

