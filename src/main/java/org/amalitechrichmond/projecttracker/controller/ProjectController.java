package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.ProjectDTO;
import org.amalitechrichmond.projecttracker.service.ProjectService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        return new ResponseEntity<>(projectService.createProject(projectDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all projects with pagination and sorting")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "deadline") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<ProjectDTO> projects = projectService.getAllProjects(page, size, sortBy, sortDir);
        return ResponseEntity.ok(projects);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project by ID")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        projectDTO.setId(id);
        return ResponseEntity.ok(projectService.updateProject(projectDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project by ID")
    public ResponseEntity<ProjectDTO> deleteProject(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(projectService.deleteProject(id));
    }
}
