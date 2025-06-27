package org.amalitechrichmond.projecttracker.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amalitechrichmond.projecttracker.DTO.AuthLoginRequestDTO;
import org.amalitechrichmond.projecttracker.DTO.AuthRegisterRequestDTO;
import org.amalitechrichmond.projecttracker.DTO.AuthResponseDTO;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.ResponseHelper;
import org.amalitechrichmond.projecttracker.model.ApiResponse;
import org.amalitechrichmond.projecttracker.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRegisterRequestDTO user) {
        try {
            UserDTO registeredUser = authService.register(user);
            if (registeredUser == null) {
                return ResponseHelper.failure("Registration failed", HttpStatus.BAD_REQUEST);
            }
            return ResponseHelper.success("User registered successfully", registeredUser);
        } catch (Exception e) {
            log.error("Registration error: {}", e.getMessage());
            return ResponseHelper.failure("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody AuthLoginRequestDTO user) {
        try {
            AuthResponseDTO response = authService.login(user);
            return ResponseHelper.success("Login successful", response);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseHelper.failure("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

}
