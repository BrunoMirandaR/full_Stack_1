package com.example.Autentication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Autentication.model.UsuarioConectado;
import com.example.Autentication.repository.UsuarioConectadoRepository;

@Service
public class UsuarioConectadoService {


    @Autowired
    public UsuarioConectadoRepository usuarioConectadoRepository;

    public List<UsuarioConectado> ListarUsuariosConectados(){
        return usuarioConectadoRepository.findAll();
    }

    public UsuarioConectado obtenerUsuarioConectadoPorId(Integer id) {
        return usuarioConectadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no conectado: " + id));
    }
    
}
