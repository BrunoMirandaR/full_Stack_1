package com.example.GestorInventario.webclient;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import reactor.core.publisher.Mono;

@Component
public class MarcaClient {
    private final WebClient webClient;

    public MarcaClient(@Value("${marca-service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Map<String, Object> obtenerMarcaPorId(Integer idMarca) {
        return webClient.get()
                .uri("/{id}", idMarca)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Marca no encontrada: " + body)))
                )
                .bodyToMono(Map.class)
                .doOnNext(body -> System.out.println("Marca obtenida por ID: " + body))
                .block();
    }

    public List<Map<String, Object>> obtenerTodasLasMarcas() {
        return webClient.get()
                .uri("")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Error al obtener marcas: " + body)))
                )
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .doOnNext(body -> System.out.println("Marcas obtenidas: " + body))
                .block();
    }
}
