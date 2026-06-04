package com.example.GestionUsuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(  "/register",
                "/api/v1/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/api-docs/**",        // según tu configuración springdoc.api-docs.path
                "/v3/api-docs/**",     // para compatibilidad con springdoc por defecto
                "/swagger-resources/**",
                "/webjars/**",
                "/swagger-config/**", "/{id}").permitAll() // Permite acceso público a estos endpoints
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
