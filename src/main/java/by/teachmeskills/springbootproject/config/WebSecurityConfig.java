package by.teachmeskills.springbootproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity(debug = true)
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Configuration
public class WebSecurityConfig {
    private JwtFilter jwtFilter;

    @Autowired
    public void setJwtFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((auth) -> {
                            try {
                                auth
                                        .requestMatchers(new AntPathRequestMatcher("/profile/**"))
                                        .authenticated()
                                        .requestMatchers(new AntPathRequestMatcher("/**/csv/**"))
                                        .hasRole("ADMIN")
                                        .anyRequest().permitAll();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

