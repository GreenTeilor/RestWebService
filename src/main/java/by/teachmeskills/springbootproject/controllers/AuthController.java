package by.teachmeskills.springbootproject.controllers;

import by.teachmeskills.springbootproject.dto.RefreshJwtRequestDto;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.teachmeskills.springbootproject.dto.JwtResponseDto;
import by.teachmeskills.springbootproject.dto.JwtRequestDto;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Login",
            description = "Give user access and refresh tokens by email and password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login is successful\tLogin is unsuccessful",
                    content = {@Content(schema = @Schema(implementation = JwtResponseDto.class)),
                            @Content(schema = @Schema(implementation = String.class))}
            ),
    })
    @PostMapping("/login")
    public JwtResponseDto login(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credentials")
                                @Valid @RequestBody JwtRequestDto authRequest,
                                BindingResult bindingResult) throws AuthorizationException {
        return authService.login(authRequest);
    }

    @Operation(
            summary = "Access token",
            description = "Get new access token by refresh token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Access token successfully obtained\tUnauthorized",
                    content = {@Content(schema = @Schema(implementation = JwtResponseDto.class)),
                            @Content(schema = @Schema(implementation = String.class))}
            ),
    })
    @PostMapping("/token")
    public JwtResponseDto getNewAccessToken(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Refresh token")
                                            @Valid @RequestBody RefreshJwtRequestDto request,
                                            BindingResult bindingResult) throws AuthorizationException {
        return authService.getAccessToken(request.getRefreshToken());
    }

    @Operation(
            summary = "Refresh token",
            description = "Get new refresh token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Refresh token successfully obtained\tUnauthorized",
                    content = {@Content(schema = @Schema(implementation = JwtResponseDto.class)),
                            @Content(schema = @Schema(implementation = String.class))}
            ),
    })
    @PostMapping("/refresh")
    public JwtResponseDto getNewRefreshToken(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Refresh token")
                                             @Valid @RequestBody RefreshJwtRequestDto request,
                                             BindingResult bindingResult) throws AuthorizationException {
        return authService.refresh(request.getRefreshToken());
    }
}
