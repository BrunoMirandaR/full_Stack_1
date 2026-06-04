package com.example.GestionUsuarios.service;

import org.springframework.stereotype.Service;

import com.example.GestionUsuarios.auth.AuthResponse;
import com.example.GestionUsuarios.model.User;
import com.example.GestionUsuarios.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public User updateUser(Integer id, User updatedUser) {
        // Verifica si el nombre de usuario ya existe para otro usuario
        Optional<User> anyUserWithSameUsername = userRepository.findByUsername(updatedUser.getUsername());
        if (anyUserWithSameUsername.isPresent() && !anyUserWithSameUsername.get().getId().equals(id)) {
            throw new RuntimeException("Error: El nombre de usuario ya existe. Por favor, elige otro.");
        }

        return userRepository.findById(id)
                .map(user -> {
                    user.setNombre(updatedUser.getNombre());
                    user.setAppaterno(updatedUser.getAppaterno());
                    user.setApmaterno(updatedUser.getApmaterno());
                    user.setRut(updatedUser.getRut());
                    user.setUsername(updatedUser.getUsername());
                    user.setPassword(updatedUser.getPassword());
                    user.setIdRol(updatedUser.getIdRol());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Error: Usuario no encontrado.");
        }
        userRepository.deleteById(id);
    }
    // mostrar todos los usuarios
    public List<User> mostrarUsuarios() {
        return userRepository.findAll();
    }

    public User obtenerPorUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario con username '" + username + "' no encontrado"));
    }
}
