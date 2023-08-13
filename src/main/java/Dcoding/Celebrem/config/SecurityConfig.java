package Dcoding.Celebrem.config;

import Dcoding.Celebrem.jwt.JwtAccessDeniedHandler;
import Dcoding.Celebrem.jwt.JwtAuthenticationEntryPoint;
import Dcoding.Celebrem.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.accessDeniedHandler(jwtAccessDeniedHandler).authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        request->request.requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/member/**").hasAnyRole("USER")
                                .requestMatchers("/").permitAll()
                                .anyRequest().permitAll()
                )
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}