package com.example.Autentication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.Autentication.repository.UsuarioConectadoRepository;

@SpringBootApplication
public class AutenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutenticationApplication.class, args);
    }

    // Este bean se ejecuta al iniciar la aplicación y limpia la tabla usuario_conectado
    @Bean
    public CommandLineRunner clearConnectedUsers(UsuarioConectadoRepository usuarioConectadoRepository) {
        return args -> usuarioConectadoRepository.deleteAll();
    }
}
