package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.config.JwtProvider;
import by.teachmeskills.springbootproject.dto.converters.UserConverter;
import by.teachmeskills.springbootproject.entities.Token;
import by.teachmeskills.springbootproject.exceptions.AuthorizationException;
import by.teachmeskills.springbootproject.repositories.TokenRepository;
import by.teachmeskills.springbootproject.repositories.UserRepository;
import by.teachmeskills.springbootproject.services.AuthService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import by.teachmeskills.springbootproject.dto.JwtRequestDto;
import by.teachmeskills.springbootproject.dto.JwtResponseDto;
import by.teachmeskills.springbootproject.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserConverter userConverter;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;

    @Override
    public JwtResponseDto login(@NonNull JwtRequestDto authRequest) throws AuthorizationException {
        UserDto user = userRepository.findByEmail(authRequest.getEmail()).map(userConverter::toDto)
                .orElseThrow(() -> new AuthorizationException("User is not found"));
        if (encoder.matches(authRequest.getPassword(), user.getPassword())) {
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            Token tokenEntity = tokenRepository.findByUsername(user.getEmail()).orElse(null);
            if (tokenEntity != null) {
                tokenEntity.setToken(refreshToken);
                tokenRepository.save(tokenEntity);
            } else {
                tokenRepository.save(Token.builder().
                        username(user.getEmail()).
                        token(refreshToken).
                        build());
            }
            return new JwtResponseDto(accessToken, refreshToken);
        } else {
            throw new AuthorizationException("Wrong password");
        }
    }

    @Override
    public JwtResponseDto getAccessToken(@NonNull String refreshToken) throws AuthorizationException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = tokenRepository.findByUsername(login).map(Token::getToken).orElse(null);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                UserDto user = userRepository.findByEmail(login).map(userConverter::toDto)
                        .orElseThrow(() -> new AuthorizationException("User is not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponseDto(accessToken, null);
            }
        }
        return new JwtResponseDto(null, null);
    }

    @Override
    public JwtResponseDto refresh(@NonNull String refreshToken) throws AuthorizationException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            Token tokenEntity = tokenRepository.findByUsername(login).orElse(null);
            if (tokenEntity != null && tokenEntity.getToken().equals(refreshToken)) {
                UserDto user = userRepository.findByEmail(login).map(userConverter::toDto)
                        .orElseThrow(() -> new AuthorizationException("User is not found"));
                String accessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                tokenEntity.setUsername(newRefreshToken);
                tokenRepository.save(tokenEntity);
                return new JwtResponseDto(accessToken, newRefreshToken);
            }
        }
        throw new AuthorizationException("Invalid jwt token");
    }

    @Override
    public Optional<UserDto> getPrincipal() {
        return userRepository.findByEmail(((User) SecurityContextHolder.getContext().getAuthentication().
                getPrincipal()).getUsername()).map(userConverter::toDto);
    }
}
