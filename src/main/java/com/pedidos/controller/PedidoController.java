package com.pedidos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final RestTemplate restTemplate;

    @Autowired
    public PedidoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Endpoint para listar produtos
    @GetMapping("/produtos")
    public ResponseEntity<String> listarProdutos() {
        String url = "http://localhost:8081/api/produtos";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }

    // Endpoint para listar clientes
    @GetMapping("/clientes")
    public ResponseEntity<String> listarClientes() {
        String url = "http://localhost:8082/clientes"; // sem /api
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }
}
