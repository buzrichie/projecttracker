package org.amalitechrichmond.projecttracker.service;


import org.amalitechrichmond.projecttracker.DTO.TaskDTO;
import org.amalitechrichmond.projecttracker.repository.TaskStatusCount;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(long id);
    TaskDTO updateTask(TaskDTO taskDTO);
    TaskDTO deleteTask(long id);
    TaskDTO assignDeveloperToTask(Long taskId, Long developerId);
    List<TaskDTO> getTasksByProjectId(Long projectId, int page, int size, String sortBy, String sortDir);
    List<TaskDTO> getTasksByDeveloperId(Long developerId, int page, int size, String sortBy, String sortDir);
    List<TaskDTO> getOverdueTasks();
    List<TaskStatusCount> getTaskStatusCounts();
    List<TaskDTO> getTasksByUserId(Long userId);
}
