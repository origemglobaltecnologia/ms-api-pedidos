package com.pedidos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "http://localhost:5173") // liberando CORS para o front
public class PedidoController {

    private final RestTemplate restTemplate;
    private final List<Map<String, Object>> pedidos = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public PedidoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // --- CRUD de Pedidos ---

    @GetMapping("")
    public ResponseEntity<List<Map<String, Object>>> listarPedidos() {
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPedido(@PathVariable Long id) {
        return pedidos.stream()
                .filter(p -> p.get("id").equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> criarPedido(@RequestBody Map<String, Object> pedido) {
        long id = counter.getAndIncrement();
        pedido.put("id", id);
        pedidos.add(pedido);
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarPedido(@PathVariable Long id,
                                                               @RequestBody Map<String, Object> dados) {
        for (int i = 0; i < pedidos.size(); i++) {
            Map<String, Object> p = pedidos.get(i);
            if (p.get("id").equals(id)) {
                dados.put("id", id);
                pedidos.set(i, dados);
                return ResponseEntity.ok(dados);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        boolean removed = pedidos.removeIf(p -> p.get("id").equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // --- Listagem de Produtos e Clientes via APIs externas ---

    @GetMapping("/produtos")
    public ResponseEntity<String> listarProdutos() {
        String url = "http://localhost:8081/api/produtos";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }

    @GetMapping("/clientes")
    public ResponseEntity<String> listarClientes() {
        String url = "http://localhost:8082/clientes";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }
}
