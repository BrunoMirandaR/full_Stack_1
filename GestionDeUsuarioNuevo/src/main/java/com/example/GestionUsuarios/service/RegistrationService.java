package com.example.GestionUsuarios.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.GestionUsuarios.auth.AuthResponse;
import com.example.GestionUsuarios.auth.RegisterRequest;
import com.example.GestionUsuarios.jwt.JwtService;
import com.example.GestionUsuarios.model.User; // Actualizado al paquete del nuevo modelo
import com.example.GestionUsuarios.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    
    public AuthResponse register(RegisterRequest request) {
        // Se construye el objeto User usando los nuevos campos del modelo
        User user = User.builder()
            .nombre(request.getNombre())
            .appaterno(request.getAppaterno())
            .apmaterno(request.getApmaterno())
            .rut(request.getRut())
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .idRol(request.getIdRol())
            .build();

        userRepository.save(user);

        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token).build();
    }
}
