package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.TaskDTO;
import org.amalitechrichmond.projecttracker.repository.TaskStatusCount;
import org.amalitechrichmond.projecttracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "CRUD operations for tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO created = taskService.createTask(taskDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task by ID")
    public ResponseEntity<TaskDTO> updateTask(@Valid @PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        taskDTO.setId(id);
        TaskDTO updated = taskService.updateTask(taskDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task by ID")
    public ResponseEntity<TaskDTO> deleteTask(@PathVariable Long id) {
        TaskDTO deleted = taskService.deleteTask(id);
        return ResponseEntity.ok(deleted);
    }

    @PutMapping("/tasks/{taskId}/assign/{developerId}")
    public ResponseEntity<TaskDTO> assignDeveloperToTask(@PathVariable Long taskId, @PathVariable Long developerId) {
        TaskDTO updatedTask = taskService.assignDeveloperToTask(taskId, developerId);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/by-project/{projectId}")
    @Operation(summary = "Get a task by project ID")
    public ResponseEntity<List<TaskDTO>> getTasksByProjectId(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<TaskDTO> tasks = taskService.getTasksByProjectId(projectId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/by-developer/{developerId}")
    @Operation(summary = "Get a task by developer ID")
    public ResponseEntity<List<TaskDTO>> getTasksByDeveloperId(
            @PathVariable Long developerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<TaskDTO> tasks = taskService.getTasksByDeveloperId(developerId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get all overdue tasks")
    public ResponseEntity<List<TaskDTO>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }


    @GetMapping("/status-summary")
    public ResponseEntity<List<TaskStatusCount>> getTaskStatusSummary() {
        return ResponseEntity.ok(taskService.getTaskStatusCounts());
    }


}
