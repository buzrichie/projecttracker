package org.amalitechrichmond.projecttracker.mapper;

import org.amalitechrichmond.projecttracker.DTO.AuthRegisterRequestDTO;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.model.User;

public class UserMapper {

    public static User toEntity(AuthRegisterRequestDTO authRegisterRequestDTO) {
        User user = new User();
        user.setName(authRegisterRequestDTO.getName());
        user.setEmail(authRegisterRequestDTO.getEmail());
        user.setPassword(authRegisterRequestDTO.getPassword());
        return user;
    }

    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public static UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    public static UserDTO toDTO(User saved) {
        return toDto(saved);
    }
}
