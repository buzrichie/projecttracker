package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.DeveloperDTO;
import org.amalitechrichmond.projecttracker.service.DeveloperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Create a new developer")
    public ResponseEntity<DeveloperDTO> createDeveloper(@RequestBody DeveloperDTO developerDTO) {
        System.out.println("Creating new developer reached");
        log.info("Creating new developer reached");
        DeveloperDTO created = developerService.createDeveloper(developerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all developers with pagination and sorting")
    public ResponseEntity<List<DeveloperDTO>> getAllDevelopers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("Get all developers reached with pagination");
        List<DeveloperDTO> developers = developerService.getAllDevelopers(page, size, sortBy, sortDir);
        return ResponseEntity.ok(developers);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a developer by ID")
    public ResponseEntity<DeveloperDTO> getDeveloperById(@PathVariable Long id) {
        return ResponseEntity.ok(developerService.getDeveloperById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a developer by ID")
    public ResponseEntity<DeveloperDTO> updateDeveloper(@Valid @PathVariable Long id, @RequestBody DeveloperDTO developerDTO) {
        developerDTO.setId(id);
        DeveloperDTO updated = developerService.updateDeveloper(developerDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a developer by ID")
    public ResponseEntity<DeveloperDTO> deleteDeveloper(@PathVariable Long id) {
        DeveloperDTO deleted = developerService.deleteDeveloper(id);
        return ResponseEntity.ok(deleted);
    }
}
