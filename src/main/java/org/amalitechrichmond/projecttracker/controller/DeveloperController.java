package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.DeveloperDTO;
import org.amalitechrichmond.projecttracker.ResponseHelper;
import org.amalitechrichmond.projecttracker.model.ApiResponse;
import org.amalitechrichmond.projecttracker.service.DeveloperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/developers")
@RequiredArgsConstructor

@Tag(name = "Developer Management", description = "CRUD operations for developers")
public class DeveloperController {

    private static final Logger log = LoggerFactory.getLogger(DeveloperController.class);
    private final DeveloperService developerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new developer")
    public ResponseEntity<ApiResponse<DeveloperDTO>> createDeveloper(@RequestBody DeveloperDTO developerDTO) {
        log.info("Creating new developer reached");
        DeveloperDTO created = developerService.createDeveloper(developerDTO);
        return ResponseHelper.success("Developer Created", created);
    }

    @GetMapping
    @Operation(summary = "Get all developers with pagination and sorting")
    public ResponseEntity<ApiResponse<List<DeveloperDTO>>> getAllDevelopers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("Get all developers reached with pagination");
        List<DeveloperDTO> developers = developerService.getAllDevelopers(page, size, sortBy, sortDir);
//        return ResponseEntity.ok(developers);
        return ResponseHelper.success("Developers retrieved successfully", developers);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a developer by ID")
    public ResponseEntity<ApiResponse<DeveloperDTO>> getDeveloperById(@PathVariable Long id) {
        DeveloperDTO developer = developerService.getDeveloperById(id);
        return ResponseHelper.success("Developer retrieved successfully", developer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a developer by ID")
    public ResponseEntity<ApiResponse<DeveloperDTO>> updateDeveloper(@Valid @PathVariable Long id, @RequestBody DeveloperDTO developerDTO) {
        developerDTO.setId(id);
        DeveloperDTO updated = developerService.updateDeveloper(developerDTO);
        return ResponseHelper.success("Developer updated successfully", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a developer by ID")
    public ResponseEntity<ApiResponse<DeveloperDTO>> deleteDeveloper(@PathVariable Long id) {
        DeveloperDTO deleted = developerService.deleteDeveloper(id);
        return ResponseHelper.success("Developer deleted successfully", deleted);
    }
    @GetMapping("/top")
    @Operation(summary = "Get top 5 developers")
    public ResponseEntity<ApiResponse<List<DeveloperDTO>>> getTopDevelopers() {
        return ResponseHelper.success("Top 5 developers retrieved", developerService.getTop5Developers());

    }
}
