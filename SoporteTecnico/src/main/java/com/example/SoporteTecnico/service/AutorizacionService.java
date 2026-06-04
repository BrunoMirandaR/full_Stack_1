package com.example.SoporteTecnico.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.SoporteTecnico.webclient.UsuarioConectadoClient;
import com.example.SoporteTecnico.webclient.UsuarioUser;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AutorizacionService {
    private final UsuarioConectadoClient usuarioConectadoClient;
    private final UsuarioUser usuarioClient;

    public AutorizacionService(UsuarioConectadoClient usuarioConectadoClient, UsuarioUser usuarioClient) {
        this.usuarioConectadoClient = usuarioConectadoClient;
        this.usuarioClient = usuarioClient;
    }

    public Integer obtenerRolUsuarioConectado(Integer idUserConectado) {
        Optional<Map<String, Object>> conectadoOpt = usuarioConectadoClient
                .buscarUsuarioConectadoPorId(idUserConectado);
        if (conectadoOpt.isEmpty() || !conectadoOpt.get().containsKey("userId")) {
            throw new RuntimeException("Usuario no conectado");
        }
        Integer idUsuario = (Integer) conectadoOpt.get().get("userId");

        Map<String, Object> usuario = usuarioClient.getUsuarioPorId(idUsuario);

        if (usuario == null || !usuario.containsKey("idRol")) {
            throw new RuntimeException("Usuario sin rol válido");
        }
        Object rolObj = usuario.get("idRol");
        Integer rol = null;
        if (rolObj instanceof Integer) {
            rol = (Integer) rolObj;
        } else if (rolObj instanceof String) {
            try {
                rol = Integer.parseInt((String) rolObj);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Rol inválido");
            }
        } else if (rolObj instanceof Number) {
            rol = ((Number) rolObj).intValue();
        }
        if (rol == null) {
            throw new RuntimeException("Rol inválido");
        }
        return rol;
    }

    public ResponseEntity<?> validarRol(Integer idUserConectado, Integer rolEsperado) {
        try {
            Integer rol = obtenerRolUsuarioConectado(idUserConectado);
            if (!rol.equals(rolEsperado)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN) // 403 Forbidden
                        .body("Acceso denegado: rol inválido");
            }
            return ResponseEntity.ok().build(); // OK sin cuerpo
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
                    .body("Acceso denegado: " + e.getMessage());
        }
    }

}
