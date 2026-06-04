package com.example.RolesyPermisos.service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.RolesyPermisos.webclient.UsuarioClient;
import com.example.RolesyPermisos.webclient.UsuarioConectadoClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AutorizacionService {
    private final UsuarioConectadoClient usuarioConectadoClient;
    private final UsuarioClient usuarioClient;

    public AutorizacionService(UsuarioConectadoClient usuarioConectadoClient, UsuarioClient usuarioClient) {
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

        Optional<Map<String, Object>> usuarioOpt = usuarioClient.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().containsKey("idRol")) {
            throw new RuntimeException("Usuario sin rol válido");
        }
        Object rolObj = usuarioOpt.get().get("idRol");
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

    public ResponseEntity<?> validarRoles(Integer idUserConectado, Set<Integer> rolesEsperados) {
        try {
            Integer rol = obtenerRolUsuarioConectado(idUserConectado);
            if (!rolesEsperados.contains(rol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Acceso denegado: rol inválido");
            }
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Acceso denegado: " + e.getMessage());
        }
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
