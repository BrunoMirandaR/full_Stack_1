package com.example.GestionUsuarios.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j // para registrar mensajes de log
@Component
public class RoleClient {
        private final WebClient webClientRol;

        public RoleClient(@Value("${rol-service.url}") String rolServiceUrl) {
                this.webClientRol = WebClient.builder()
                                .baseUrl(rolServiceUrl)
                                .build();
        }

        public Map<String, Object> obtenerRolPorNombre(String nombreRol, Integer idUserConectado) {
                return webClientRol.get()
                                .uri("/nombre/{nombre}", nombreRol)
                                .header("X-User-Id", String.valueOf(idUserConectado))
                                .retrieve()
                                .onStatus(
                                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                                response -> response.bodyToMono(String.class)
                                                                .flatMap(body -> Mono.error(new RuntimeException(
                                                                                "Error al obtener rol: " + body))))
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                                })
                                .doOnNext(body -> log.info("Rol obtenido: {}", body))
                                .block();
        }

        public Map<String, Object> obtenerRolPorId(Integer idRol, Integer idUserConectado) {
                return webClientRol.get()
                                .uri("/{id}", idRol)
                                .header("X-User-Id", String.valueOf(idUserConectado))
                                .retrieve()
                                .onStatus(
                                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                                response -> response.bodyToMono(String.class)
                                                                .flatMap(body -> Mono.error(new RuntimeException(
                                                                                "Error al obtener rol: " + body))))
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                                })
                                .block();
        }

        public Map<String, Object> obtenerRolPorNombreSinValidacion(String nombreRol) {
                return webClientRol.get()
                                .uri("/nombre/{nombre}", nombreRol)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                                })
                                .block();
        }
}
