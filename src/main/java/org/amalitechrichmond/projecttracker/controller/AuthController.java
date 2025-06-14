package org.amalitechrichmond.projecttracker.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amalitechrichmond.projecttracker.DTO.AuthLoginRequestDTO;
import org.amalitechrichmond.projecttracker.DTO.AuthRegisterRequestDTO;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService ;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRegisterRequestDTO user) {
        try {
            UserDTO authRequestDTO = authService.register(user);
            if (authRequestDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(authRequestDTO, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequestDTO user) {
        try {
            String accessToken = authService.login(user);
            if (accessToken == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(accessToken, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
