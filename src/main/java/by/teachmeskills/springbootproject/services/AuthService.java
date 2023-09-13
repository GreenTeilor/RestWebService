package by.teachmeskills.springbootproject.services;

import by.teachmeskills.springbootproject.dto.JwtRequestDto;
import by.teachmeskills.springbootproject.dto.JwtResponseDto;
import by.teachmeskills.springbootproject.dto.UserDto;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import lombok.NonNull;

import java.util.Optional;


public interface AuthService {
    JwtResponseDto login(@NonNull JwtRequestDto authRequest) throws AuthorizationException;
    JwtResponseDto getAccessToken(@NonNull String refreshToken) throws AuthorizationException;
    JwtResponseDto refresh(@NonNull String refreshToken) throws AuthorizationException;
    Optional<UserDto> getPrincipal();
}
