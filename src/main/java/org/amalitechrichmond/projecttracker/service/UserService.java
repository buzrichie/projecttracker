package org.amalitechrichmond.projecttracker.service;

import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.model.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers(int page, int size, String sortBy, String sortDir);
    UserDTO getUserById(long id);
    UserDTO updateUser(UserDTO userDTO);
    UserDTO deleteUser(long id);

    UserDTO getLoggedInUser();
}
