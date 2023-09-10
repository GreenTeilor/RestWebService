package by.teachmeskills.springbootproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequestDto {
    @NotBlank(message = "Blank request email")
    private String email;

    @NotBlank(message = "Blank request password")
    private String password;
}
