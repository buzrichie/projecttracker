package org.amalitechrichmond.projecttracker.config;

import lombok.AllArgsConstructor;
import org.amalitechrichmond.projecttracker.exception.ResourceNotFoundException;
import org.amalitechrichmond.projecttracker.filter.JwtAuthenticationFilter;
import org.amalitechrichmond.projecttracker.security.OAuth2LoginSuccessService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private OAuth2LoginSuccessService oAuth2LoginSuccessService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ResourceNotFoundException.JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors->cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedMethods(List.of("Get","Post", "Put", "Delete", "Patch"));
                    configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/auth/oauth2/success",
                                "/actuator/**"
                        ).permitAll()
                        .requestMatchers("/swagger/**","/swagger-ui/**","/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/projects/**")
                        .hasAnyAuthority("MANAGER", "ADMIN")
                        .requestMatchers("/api/tasks/**")
                        .hasAnyAuthority("DEVELOPER", "ADMIN")


                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessService)
                ).exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

