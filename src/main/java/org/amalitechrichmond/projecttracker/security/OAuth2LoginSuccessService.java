package org.amalitechrichmond.projecttracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.amalitechrichmond.projecttracker.DTO.AuthResponseDTO;
import org.amalitechrichmond.projecttracker.enums.UserRole;
import org.amalitechrichmond.projecttracker.model.User;
import org.amalitechrichmond.projecttracker.provider.JwtTokenProvider;
import org.amalitechrichmond.projecttracker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class OAuth2LoginSuccessService implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, java.io.IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(email, oAuth2User));

        String token = jwtTokenProvider.generateToken(
                user.getId().toString(),
                Map.of("email", user.getEmail(), "role", user.getRole())
        );

        AuthResponseDTO authResponse = AuthResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .username(user.getName())
                .role(user.getRole().name())
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), authResponse);
    }

    private User registerNewUser(String email, OAuth2User oAuth2User) {
        User user = new User();
        user.setEmail(email);
        user.setName((String) oAuth2User.getAttributes().get("name"));
        user.setRole(UserRole.ROLE_CONTRACTOR);
        // fix by removal
        String dummyPassword = "password";
        user.setPassword(dummyPassword);

        return userRepository.save(user);
    }

}
