package org.amalitechrichmond.projecttracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.service.UserService;
import org.springframework.http.HttpStatus;
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


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new User")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO UserDTO) {
        UserDTO created = userService.createUser(UserDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all users with pagination and sorting")
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<UserDTO> users = userService.getAllUsers(page, size, sortBy, sortDir);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    @Operation(summary = "Get a User by ID")
    public ResponseEntity<UserDTO> getLoggedInUser() {
        return ResponseEntity.ok(userService.getLoggedInUser());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a User by ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a User by ID")
    public ResponseEntity<UserDTO> updateUser(@Valid @PathVariable Long id, @RequestBody UserDTO UserDTO) {
        UserDTO.setId(id);
        UserDTO updated = userService.updateUser(UserDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a User by ID")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        UserDTO deleted = userService.deleteUser(id);
        return ResponseEntity.ok(deleted);
    }
}
