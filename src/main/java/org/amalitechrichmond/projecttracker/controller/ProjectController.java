package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.ProjectDTO;
import org.amalitechrichmond.projecttracker.ResponseHelper;
import org.amalitechrichmond.projecttracker.model.ApiResponse;
import org.amalitechrichmond.projecttracker.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "CRUD operations for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        ProjectDTO created = projectService.createProject(projectDTO);
        return ResponseHelper.success("Project created successfully", created);
    }

    @GetMapping
    @Operation(summary = "Get all projects with pagination and sorting")
    public ResponseEntity<ApiResponse<List <ProjectDTO>>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "deadline") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<ProjectDTO> projects = projectService.getAllProjects(page, size, sortBy, sortDir);
        return ResponseHelper.success("Projects retrieved successfully", projects);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project by ID")
    public ResponseEntity<ApiResponse<ProjectDTO>> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        projectDTO.setId(id);
        ProjectDTO updated = projectService.updateProject(projectDTO);
        return ResponseHelper.success("Project updated successfully", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project by ID")
    public ResponseEntity<ApiResponse<ProjectDTO>> deleteProject(@Valid @PathVariable Long id) {
        ProjectDTO deleted = projectService.deleteProject(id);
        return ResponseHelper.success("Project deleted successfully", deleted);
    }

    @GetMapping("/without-tasks")
    @Operation(summary = "Get all projects without tasks")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getProjectsWithoutTasks() {
        List<ProjectDTO> projects = projectService.getProjectsWithoutTasks();
        return ResponseHelper.success("Projects without tasks retrieved", projects);
    }
}
