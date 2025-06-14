package org.amalitechrichmond.projecttracker.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Component
@Getter
@Setter
public class AuthLoginRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
