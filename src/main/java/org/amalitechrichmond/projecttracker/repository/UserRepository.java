package org.amalitechrichmond.projecttracker.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.amalitechrichmond.projecttracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIs(String email);
    boolean existsByNameIs(String username);
    List<User> findFirstByEmail(String email);

    Optional<User> findByEmail(String email);
}
