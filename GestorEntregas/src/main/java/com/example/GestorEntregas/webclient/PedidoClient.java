package com.example.GestorEntregas.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class PedidoClient {
    private final WebClient webClient;

    public PedidoClient(@Value("${pedidos-service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Map<String, Object> obtenerPedidoPorId(Integer idPedido) {
        try {
            return webClient.get()
                    .uri("/{idPedido}", idPedido)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException("Pedido no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al llamar a microservicio Pedidos: " + e.getMessage());
        }
    }
}
