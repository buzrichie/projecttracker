package org.amalitechrichmond.projecttracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.TaskDTO;
import org.amalitechrichmond.projecttracker.exception.ResourceNotFoundException;
import org.amalitechrichmond.projecttracker.mapper.TaskMapper;
import org.amalitechrichmond.projecttracker.model.Developer;
import org.amalitechrichmond.projecttracker.model.Project;
import org.amalitechrichmond.projecttracker.model.Task;
import org.amalitechrichmond.projecttracker.repository.DeveloperRepository;
import org.amalitechrichmond.projecttracker.repository.ProjectRepository;
import org.amalitechrichmond.projecttracker.repository.TaskRepository;
import org.amalitechrichmond.projecttracker.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final DeveloperRepository developerRepository;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Project project = projectRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        Set<Developer> developers = fetchDevelopersByIds(taskDTO.getDeveloperIds());
        Task task = TaskMapper.toEntity(taskDTO, project, developers);
        Task savedTask = taskRepository.save(task);

        return TaskMapper.toDTO(savedTask);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return TaskMapper.toDTO(task);
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(taskDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Update fields
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setStatus(taskDTO.getStatus());
        existingTask.setDueDate(taskDTO.getDueDate());

        if (taskDTO.getProjectId() != null) {
            Project project = projectRepository.findById(taskDTO.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
            existingTask.setProject(project);
        }

        if (taskDTO.getDeveloperIds() != null) {
            Set<Developer> developers = fetchDevelopersByIds(taskDTO.getDeveloperIds());
            existingTask.setDevelopers(developers);
        }

        return TaskMapper.toDTO(taskRepository.save(existingTask));
    }

    @Override
    public TaskDTO deleteTask(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
        return TaskMapper.toDTO(task);
    }

    //  Utility method to fetch developer entities from IDs
    private Set<Developer> fetchDevelopersByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return new HashSet<>(developerRepository.findAllById(ids));
    }
}
