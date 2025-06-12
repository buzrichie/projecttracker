package org.amalitechrichmond.projecttracker.service.impl;

import lombok.AllArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.mapper.UserMapper;
import org.amalitechrichmond.projecttracker.model.User;
import org.amalitechrichmond.projecttracker.repository.UserRepository;
import org.amalitechrichmond.projecttracker.service.AuthService;
import org.amalitechrichmond.projecttracker.provider.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        System.out.println("UserId for subject "+ user.getId());
        return jwtTokenProvider.generateToken(String.valueOf(user.getId()), claims);
    }


    @Override
    public UserDTO register(UserDTO userDTO) {
        if(userRepository.existsByEmailIs(userDTO.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByNameIs(userDTO.getName())){
            throw new RuntimeException("Name already exists");
        }
        User user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return UserMapper.toDto(userRepository.save(user));
    }


}
