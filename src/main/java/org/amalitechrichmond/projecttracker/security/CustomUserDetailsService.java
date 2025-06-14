package org.amalitechrichmond.projecttracker.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amalitechrichmond.projecttracker.model.User;
import org.amalitechrichmond.projecttracker.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Load user from Database
        log.info("Loading user by email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        log.info("User: " + user);
        return new UserDetailsImpl(user);
    }
}

