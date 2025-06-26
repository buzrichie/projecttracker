package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.TaskDTO;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.ResponseHelper;
import org.amalitechrichmond.projecttracker.model.ApiResponse;
import org.amalitechrichmond.projecttracker.service.TaskService;
import org.amalitechrichmond.projecttracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "CRUD operations for users")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new User")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        UserDTO created = userService.createUser(userDTO);
        return ResponseHelper.success("User created successfully", created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all users with pagination and sorting")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<UserDTO> users = userService.getAllUsers(page, size, sortBy, sortDir);
        return ResponseHelper.success("Users retrieved successfully", users);
    }

    @GetMapping("/me")
    @Operation(summary = "Get the currently logged-in user")
    public ResponseEntity<ApiResponse<UserDTO>> getLoggedInUser() {
        return ResponseHelper.success("Logged-in user retrieved", userService.getLoggedInUser());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        return ResponseHelper.success("User retrieved", userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a user by ID")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@Valid @PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        UserDTO updated = userService.updateUser(userDTO);
        return ResponseHelper.success("User updated successfully", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID")
    public ResponseEntity<ApiResponse<UserDTO>> deleteUser(@PathVariable Long id) {
        UserDTO deleted = userService.deleteUser(id);
        return ResponseHelper.success("User deleted successfully", deleted);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get a user Tasks")
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getTasksByUser(@PathVariable("id") Long userId) {
        List<TaskDTO> tasks = taskService.getTasksByUserId(userId);
        return ResponseHelper.success("" ,tasks);
    }
}
