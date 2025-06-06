package org.amalitechrichmond.projecttracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Project Tracker API")
                        .description("Spring Boot REST API for Project Tracker")
                        .version("1.0"));
    }
}
