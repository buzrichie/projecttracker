package org.amalitechrichmond.projecttracker.service;

import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    String login(UserDTO user);
    UserDTO register(UserDTO user);
}
