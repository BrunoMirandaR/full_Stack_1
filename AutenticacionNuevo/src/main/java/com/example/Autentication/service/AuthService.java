package com.example.Autentication.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.Autentication.auth.AuthResponse;
import com.example.Autentication.auth.LoginRequest;
import com.example.Autentication.jwt.JwtService;
import com.example.Autentication.model.User;
import com.example.Autentication.model.UsuarioConectado;
import com.example.Autentication.repository.UserRepository;
import com.example.Autentication.repository.UsuarioConectadoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UsuarioConectadoRepository usuarioConectadoRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthResponse login(LoginRequest request) {
        // Autentica el usuario
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        // Busca el usuario en la base de datos
        User usuario = userRepository.findByUsername(request.getUsername())
                              .orElseThrow(() -> new RuntimeException("User not found"));
                              
        // Genera el token JWT
        String token = jwtService.getToken(usuario);
        
        // Guarda el login exitoso en la tabla usuario_conectado
        UsuarioConectado registro = UsuarioConectado.builder()
                                        .userId(usuario.getId())
                                        .username(usuario.getUsername())
                                        .token(token)
                                        .build();
        usuarioConectadoRepository.save(registro);
        
        return AuthResponse.builder().token(token).build();
    }


}
    