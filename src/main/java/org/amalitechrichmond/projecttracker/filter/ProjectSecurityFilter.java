//package org.amalitechrichmond.projecttracker.filter;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@Order(2)
//public class ProjectSecurityFilter {
//    @Configuration
//    @EnableWebSecurity
//    @Order(2)
//    public class ProjectSecurityFilter {
//
//        @Bean
//        public SecurityFilterChain tasksFilterChain(HttpSecurity http) throws Exception {
//            http
//                    .securityMatcher("/tasks/**")
//                    .authorizeHttpRequests(authorize -> authorize
//                            .requestMatchers("/tasks/admin").hasRole("ADMIN")
//                            .requestMatchers("/tasks/**").hasAnyRole("USER", "ADMIN")
//                            .anyRequest().authenticated()
//                    );
//            return http.build();
//        }
//    }
//
//}
