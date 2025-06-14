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
import org.amalitechrichmond.projecttracker.repository.TaskStatusCount;
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.amalitechrichmond.projecttracker.service.TaskService;
import org.amalitechrichmond.projecttracker.util.AccessChecker;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final DeveloperRepository developerRepository;
    private final AuditLogService auditLogService;
    private final AccessChecker accessChecker;

    @Override
    @Transactional
    @CacheEvict(value = {"tasks", "taskById", "tasksByProject", "tasksByDeveloper", "overdueTasks", "taskStatusCounts"}, allEntries = true)
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
    @Transactional(readOnly = true)
    @Cacheable(value = "tasks")
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "taskById", key = "#id")
    public TaskDTO getTaskById(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Unauthenticated access.");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            String currentUserId = authentication.getName();
            long userIdLong;
            try {
                userIdLong = Long.parseLong(currentUserId);
            } catch (NumberFormatException e) {
                throw new AccessDeniedException("Invalid user ID.");
            }

            boolean isOwner = task.getDevelopers().stream()
                    .anyMatch(dev -> dev.getId().equals(userIdLong));

            if (!isOwner) {
                throw new AccessDeniedException("You do not own this task.");
            }
        }

        return TaskMapper.toDTO(task);
    }


    @Override
    @Transactional
    @CacheEvict(value = {"tasks", "taskById", "tasksByProject", "tasksByDeveloper", "overdueTasks", "taskStatusCounts"}, key = "#taskDTO.id", allEntries = true)
    public TaskDTO updateTask(TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(taskDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (existingTask.getDevelopers().stream().noneMatch(dev -> accessChecker.isOwner(dev.getEmail()))) {
            throw new AccessDeniedException("You do not own this task.");
        }
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
    @Transactional
    @CacheEvict(value = {"tasks", "taskById", "tasksByProject", "tasksByDeveloper", "overdueTasks", "taskStatusCounts"}, key = "#id", allEntries = true)
    public TaskDTO deleteTask(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
        auditLogService.saveLog("DELETE","Task",String.valueOf(id), task,"developer");
        return TaskMapper.toDTO(task);
    }

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    @CacheEvict(value = {"tasks", "taskById", "tasksByProject", "tasksByDeveloper", "overdueTasks", "taskStatusCounts"}, key = "#taskId", allEntries = true)
    public List<TaskDTO> getTasksByProjectId(Long projectId, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Task> tasks = taskRepository.findByProjectId(projectId, pageable);
        return tasks.stream().map(TaskMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "tasksByProject", key = "#projectId + '_' + #page + '_' + #size + '_' + #sortBy + '_' + #sortDir")
    public List<TaskDTO> getTasksByDeveloperId(Long developerId, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Task> tasks = taskRepository.findByDevelopers_Id(developerId, pageable);
        return tasks.stream().map(TaskMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "tasksByDeveloper", key = "#developerId + '_' + #page + '_' + #size + '_' + #sortBy + '_' + #sortDir")
    public List<TaskDTO> getOverdueTasks() {
        return taskRepository.findOverdueTasks()
                .stream()
                .map(TaskMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "overdueTasks")
    public List<TaskStatusCount> getTaskStatusCounts() {
        return taskRepository.countTasksByStatus();
    }


    //  Utility method to fetch developer entities from IDs
    private Set<Developer> fetchDevelopersByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return new HashSet<>(developerRepository.findAllById(ids));
    }
}
