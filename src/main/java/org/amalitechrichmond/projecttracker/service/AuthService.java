package org.amalitechrichmond.projecttracker.service;

import org.amalitechrichmond.projecttracker.DTO.AuthLoginRequestDTO;
import org.amalitechrichmond.projecttracker.DTO.AuthRegisterRequestDTO;
import org.amalitechrichmond.projecttracker.DTO.AuthResponseDTO;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponseDTO login(AuthLoginRequestDTO user);
    UserDTO register(AuthRegisterRequestDTO user);
}
