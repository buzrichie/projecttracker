package org.amalitechrichmond.projecttracker.security;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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
        System.out.println("used authentication oauth2");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // Register if new
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(email,oAuth2User));

        // Generate JWT
        String token = jwtTokenProvider.generateToken(user.getId().toString(), Map.of("email", user.getEmail(), "role", user.getRole()));
        response.sendRedirect("http://localhost:3000/oauth2/success?token=" + token); // or redirect to your frontend
    }

    private User registerNewUser(String email, OAuth2User oAuth2User) {
        User user = new User();
        user.setEmail(email);
        user.setName((String) oAuth2User.getAttributes().get("name")); // Google returns 'name'
        user.setRole(UserRole.ROLE_CONTRACTOR);
        // fix by removal
        String dummyPassword = "password";
        user.setPassword(dummyPassword);

        return userRepository.save(user);
    }

}
