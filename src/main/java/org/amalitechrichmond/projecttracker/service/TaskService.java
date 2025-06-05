package org.amalitechrichmond.projecttracker.service;


import org.amalitechrichmond.projecttracker.DTO.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(long id);
    TaskDTO updateTask(TaskDTO taskDTO);
    TaskDTO deleteTask(long id);
}
