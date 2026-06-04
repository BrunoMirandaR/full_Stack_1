package com.example.SoporteTecnico.webclient;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.beans.factory.annotation.Value;

@Component
public class UsuarioUser {

    private final WebClient webClient;

    //metodo constructor 
    public UsuarioUser(@Value("${usuario-service.url}") String usuarioServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(usuarioServiceUrl).build();
    }
    //metodo para realizar la consulta getmaping(/{id}) al microservicio estado
    public Map<String, Object> getUsuarioPorId(Integer id){
        return this.webClient.get()
        .uri("/{id}", id)
        .retrieve()
        .onStatus(status -> status.is4xxClientError(),
        response -> response.bodyToMono(String.class)
        .map(body -> new RuntimeException("Usuario no encontrado")))
        .bodyToMono(Map.class)
        .doOnNext(body -> System.out.println("Respuesta usuario id: " + body))
        .block();

    }
}
