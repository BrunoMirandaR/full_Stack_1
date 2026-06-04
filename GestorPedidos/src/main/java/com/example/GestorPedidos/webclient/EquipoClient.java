package com.example.GestorPedidos.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class EquipoClient {
    private final WebClient webClientEquipo;

    public EquipoClient(@Value("${equipo-service.url}") String equipoServiceUrl) {
        this.webClientEquipo = WebClient.builder()
                .baseUrl(equipoServiceUrl)
                .build();
    }

    public Map<String, Object> obtenerEquipoPorId(Integer idEquipo) {
        return webClientEquipo.get()
                .uri("/{idEquipo}", idEquipo)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Error al obtener equipo: " + body))))
                .bodyToMono(Map.class)
                .doOnNext(body -> System.out.println("Equipo obtenido: " + body))
                .block();
    }

    public Map<String, Object> obtenerEstadoPorId(Integer idEstado) {
        return webClientEquipo.get()
                .uri("/estados/{idEstado}", idEstado)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Error al obtener estado: " + body))))
                .bodyToMono(Map.class)
                .doOnNext(body -> System.out.println("Estado obtenido: " + body))
                .block();
    }

}
