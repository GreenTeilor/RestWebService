package by.teachmeskills.springbootproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponseDto {
    @NotBlank(message = "Blank response bearer")
    private final String type = "Bearer";

    @NotBlank(message = "Blank response accessToken")
    private String accessToken;

    @NotBlank(message = "Blank response refreshToken")
    private String refreshToken;
}
