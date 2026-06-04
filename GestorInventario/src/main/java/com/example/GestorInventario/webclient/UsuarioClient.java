package com.example.GestorInventario.webclient;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class UsuarioClient {
    private final WebClient webClient;

    public UsuarioClient(@Value("${usuario-service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Optional<Map<String, Object>> obtenerUsuarioPorId(Integer id) {
        try {
            Map<String, Object> usuario = webClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException("Error al obtener usuario: " + body)))
                    )
                    .bodyToMono(Map.class)
                    .doOnNext(body -> System.out.println("Usuario obtenido: " + body))
                    .block();

            return Optional.ofNullable(usuario);
            
        } catch (Exception e) {
            System.err.println("Error al buscar usuario por ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }
}
