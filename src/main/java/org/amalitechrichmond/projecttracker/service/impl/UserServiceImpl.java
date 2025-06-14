package org.amalitechrichmond.projecttracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.UserDTO;
import org.amalitechrichmond.projecttracker.exception.ResourceNotFoundException;
import org.amalitechrichmond.projecttracker.mapper.UserMapper;
import org.amalitechrichmond.projecttracker.model.User;
import org.amalitechrichmond.projecttracker.repository.UserRepository;
import org.amalitechrichmond.projecttracker.service.AuditLogService;
import org.amalitechrichmond.projecttracker.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    @CacheEvict(value = {"Users", "UserById", "top5Users"}, allEntries = true)
    public UserDTO createUser(UserDTO userDTO) {
        User User = UserMapper.toEntity(userDTO);
        User saved = userRepository.save(User);
        auditLogService.saveLog("CREATE","User",String.valueOf(saved.getId()), saved,"User");
        return UserMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "Users", key = "#page + '_' + #size + '_' + #sortBy + '_' + #sortDir")
    public List<UserDTO> getAllUsers(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserById", key = "#id")
    public UserDTO getUserById(long id) {
        User User = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return UserMapper.toDTO(User);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"Users", "UserById", "top5Users"}, key = "#userDTO.id", allEntries = true)
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userDTO.getId()));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        User updated = userRepository.save(user);
        auditLogService.saveLog("UPDATE","User",String.valueOf(updated.getId()), updated,"User");
        return UserMapper.toDTO(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"Users", "UserById", "top5Users"}, key = "#id", allEntries = true)
    public UserDTO deleteUser(long id) {
        User User = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        userRepository.delete(User);
        auditLogService.saveLog("DELETE","User",String.valueOf(id), User,"User");
        return UserMapper.toDTO(User);
    }

    @Override
    public UserDTO getLoggedInUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("No authenticated user found.");
            }

            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserDTO userDTO = UserMapper.toDTO(user);
            userDTO.setPassword(null);
            return userDTO;
        }
        catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }



}
