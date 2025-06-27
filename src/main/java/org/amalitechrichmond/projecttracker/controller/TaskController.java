package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.TaskDTO;
import org.amalitechrichmond.projecttracker.ResponseHelper;
import org.amalitechrichmond.projecttracker.model.ApiResponse;
import org.amalitechrichmond.projecttracker.repository.TaskStatusCount;
import org.amalitechrichmond.projecttracker.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "CRUD operations for tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO created = taskService.createTask(taskDTO);
        return ResponseHelper.success("Task created successfully", created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get all tasks")
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getAllTasks() {
        return ResponseHelper.success("All tasks retrieved", taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@PathVariable Long id) {
        return ResponseHelper.success("Task retrieved", taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task by ID")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTask(@Valid @PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        taskDTO.setId(id);
        TaskDTO updated = taskService.updateTask(taskDTO);
        return ResponseHelper.success("Task updated", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task by ID")
    public ResponseEntity<ApiResponse<TaskDTO>> deleteTask(@PathVariable Long id) {
        TaskDTO deleted = taskService.deleteTask(id);
        return ResponseHelper.success("Task deleted", deleted);
    }

    @PutMapping("/tasks/{taskId}/assign/{developerId}")
    @Operation(summary = "Assign developer to task")
    public ResponseEntity<ApiResponse<TaskDTO>> assignDeveloperToTask(@PathVariable Long taskId, @PathVariable Long developerId) {
        TaskDTO updatedTask = taskService.assignDeveloperToTask(taskId, developerId);
        return ResponseHelper.success("Developer assigned to task", updatedTask);
    }

    @GetMapping("/by-project/{projectId}")
    @Operation(summary = "Get tasks by project ID")
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getTasksByProjectId(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<TaskDTO> tasks = taskService.getTasksByProjectId(projectId, page, size, sortBy, sortDir);
        return ResponseHelper.success("Tasks for project retrieved", tasks);
    }

    @GetMapping("/by-developer/{developerId}")
    @Operation(summary = "Get tasks by developer ID")
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getTasksByDeveloperId(
            @PathVariable Long developerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<TaskDTO> tasks = taskService.getTasksByDeveloperId(developerId, page, size, sortBy, sortDir);
        return ResponseHelper.success("Tasks for developer retrieved", tasks);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get all overdue tasks")
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getOverdueTasks() {
        return ResponseHelper.success("Overdue tasks retrieved", taskService.getOverdueTasks());
    }

    @GetMapping("/status-summary")
    @Operation(summary = "Get task status summary")
    public ResponseEntity<ApiResponse<List<TaskStatusCount>>> getTaskStatusSummary() {
        return ResponseHelper.success("Task status summary retrieved", taskService.getTaskStatusCounts());
    }
}
