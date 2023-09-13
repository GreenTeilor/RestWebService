package by.teachmeskills.springbootproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequestDto {
    @NotBlank(message = "Blank response refreshToken")
    public String refreshToken;
}
