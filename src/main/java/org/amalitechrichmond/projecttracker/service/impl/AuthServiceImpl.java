package org.amalitechrichmond.projecttracker.service.impl;

import lombok.AllArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.AuthLoginRequestDTO;
import org.amalitechrichmond.projecttracker.DTO.AuthRegisterRequestDTO;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.enums.UserRole;
import org.amalitechrichmond.projecttracker.exception.EmailAlreadyExist;
import org.amalitechrichmond.projecttracker.mapper.UserMapper;
import org.amalitechrichmond.projecttracker.model.User;
import org.amalitechrichmond.projecttracker.repository.UserRepository;
import org.amalitechrichmond.projecttracker.service.AuthService;
import org.amalitechrichmond.projecttracker.provider.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    JwtTokenProvider jwtTokenProvider;
    AuthenticationManager authenticationManager;

    @Override
    public String login(AuthLoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        return jwtTokenProvider.generateToken(authentication.getName());
    }


    @Override
    public UserDTO register(AuthRegisterRequestDTO request) {
        checkForExistingData(request);
        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ROLE_DEVELOPER);

        return UserMapper.toDto(userRepository.save(user));
    }

    private void checkForExistingData(AuthRegisterRequestDTO request) {
        if(userRepository.existsByEmailIs(request.getEmail())){
            throw new EmailAlreadyExist("Email already exists");
        }
    }


}
