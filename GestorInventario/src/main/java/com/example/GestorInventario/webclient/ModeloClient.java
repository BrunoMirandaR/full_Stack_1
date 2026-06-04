package com.example.GestorInventario.webclient;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class ModeloClient {

    private final WebClient webClient;

    public ModeloClient(@Value("${modelo-service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Map<String, Object> obtenerModeloPorId(Integer idModelo) {
        return webClient.get()
                .uri("/{id}", idModelo)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Modelo no encontrado: " + body)))
                )
                .bodyToMono(Map.class)
                .doOnNext(body -> System.out.println("Modelo obtenido por ID: " + body))
                .block();
    }

    public List<Map<String, Object>> obtenerTodosLosModelos() {
        return webClient.get()
                .uri("")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Error al obtener modelos: " + body)))
                )
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .doOnNext(body -> System.out.println("Modelos obtenidos: " + body))
                .block();
    }
}
