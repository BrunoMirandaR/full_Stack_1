package com.example.SoporteTecnico.webclient;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class UsuarioConectadoClient {
    private final WebClient webClient;

    public UsuarioConectadoClient(@Value("${usuario-conectado-service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Optional<Map<String, Object>> buscarUsuarioConectadoPorId(Integer id) {
        try {
            Map<String, Object> result = webClient.get()
                    .uri("/auth/{id}", id)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(
                                            new RuntimeException("Error al obtener usuario conectado: " + body))))
                    .bodyToMono(Map.class)
                    .doOnNext(body -> System.out.println("Usuario conectado por ID: " + body))
                    .block();

            System.out.println("Resultado obtenido (post-block): " + result);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            System.err.println("Fallo al obtener usuario conectado por ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }
}
