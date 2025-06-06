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
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.amalitechrichmond.projecttracker.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final AuditLogService auditLogService;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Project project = projectRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        Set<Developer> developers = fetchDevelopersByIds(taskDTO.getDeveloperIds());
        Task task = TaskMapper.toEntity(taskDTO, project, developers);
        Task savedTask = taskRepository.save(task);
        auditLogService.saveLog("CREATE","Task",String.valueOf(savedTask.getId()), savedTask,"developer");
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
        auditLogService.saveLog("UPDATE","Task",String.valueOf(existingTask.getId()), existingTask,"developer");
        return TaskMapper.toDTO(taskRepository.save(existingTask));
    }

    @Override
    public TaskDTO deleteTask(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
        auditLogService.saveLog("DELETE","Task",String.valueOf(id), task,"developer");
        return TaskMapper.toDTO(task);
    }

    @Override
    public TaskDTO assignDeveloperToTask(Long taskId, Long developerId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id: " + developerId));

        // Initialize if null
        if (task.getDevelopers() == null) {
            task.setDevelopers(new HashSet<>());
        }

        task.getDevelopers().add(developer);
        Task updatedTask = taskRepository.save(task);

        // Audit (Optional)
        auditLogService.saveLog(
                "UPDATE",
                "Task",
                task.getId().toString(),
                updatedTask,
                "developer"
        );

        return TaskMapper.toDTO(updatedTask);
    }

    @Override
    public List<TaskDTO> getTasksByProjectId(Long projectId, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Task> tasks = taskRepository.findByProjectId(projectId, pageable);
        return tasks.stream().map(TaskMapper::toDTO).toList();
    }

    @Override
    public List<TaskDTO> getTasksByDeveloperId(Long developerId, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Task> tasks = taskRepository.findByDevelopers_Id(developerId, pageable);
        return tasks.stream().map(TaskMapper::toDTO).toList();
    }


    //  Utility method to fetch developer entities from IDs
    private Set<Developer> fetchDevelopersByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return new HashSet<>(developerRepository.findAllById(ids));
    }
}
