package by.teachmeskills.springbootproject.config;

import by.teachmeskills.springbootproject.entities.Role;
import by.teachmeskills.springbootproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import by.teachmeskills.springbootproject.entities.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private PasswordEncoder encoder;

    @Autowired
    @Lazy
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && jwtProvider.validateAccessToken(token)) {
            String email = jwtProvider.getAccessClaims(token).getSubject();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                org.springframework.security.core.userdetails.User principal =
                        new org.springframework.security.core.userdetails.User(
                                user.getEmail(), encoder.encode(user.getPassword()),
                                user.getRoles().stream().map(Role::getName).map(SimpleGrantedAuthority::new).toList());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal,
                        null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
