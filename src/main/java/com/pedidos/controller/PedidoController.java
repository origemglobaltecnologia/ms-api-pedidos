package com.pedidos.controller;

import com.pedidos.model.Pedido;
import com.pedidos.model.ProdutoPedido;
import com.pedidos.model.StatusPedido;
import com.pedidos.repository.PedidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "http://localhost:5173")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final RestTemplate restTemplate;

    public PedidoController(PedidoRepository pedidoRepository, RestTemplate restTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("")
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedido(@PathVariable UUID id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<Pedido> criarPedido(@RequestBody Map<String, Object> dados) {
        UUID clienteId = UUID.fromString((String) dados.get("clienteId"));
        List<Map<String, Object>> produtosJson = (List<Map<String, Object>>) dados.get("produtos");

        List<ProdutoPedido> produtos = new ArrayList<>();
        for (Map<String, Object> p : produtosJson) {
            UUID produtoId = UUID.fromString((String) p.get("produtoId"));
            Integer quantidade = (Integer) p.get("quantidade");
            produtos.add(new ProdutoPedido(produtoId, quantidade));
        }

        Pedido pedido = new Pedido(
                clienteId,
                produtos,
                LocalDateTime.now(),
                StatusPedido.PENDENTE
        );

        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable UUID id, @RequestBody Map<String, Object> dados) {
        Optional<Pedido> existente = pedidoRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pedido pedido = existente.get();

        if (dados.containsKey("status")) {
            pedido.setStatus(StatusPedido.valueOf((String) dados.get("status")));
        }

        if (dados.containsKey("produtos")) {
            List<Map<String, Object>> produtosJson = (List<Map<String, Object>>) dados.get("produtos");
            List<ProdutoPedido> produtos = new ArrayList<>();
            for (Map<String, Object> p : produtosJson) {
                UUID produtoId = UUID.fromString((String) p.get("produtoId"));
                Integer quantidade = (Integer) p.get("quantidade");
                produtos.add(new ProdutoPedido(produtoId, quantidade));
            }
            pedido.setProdutos(produtos);
        }

        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable UUID id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/produtos")
    public ResponseEntity<String> listarProdutos() {
        String url = "http://localhost:8081/api/produtos";
        return restTemplate.getForEntity(url, String.class);
    }

    @GetMapping("/clientes")
    public ResponseEntity<String> listarClientes() {
        String url = "http://localhost:8082/clientes";
        return restTemplate.getForEntity(url, String.class);
    }
}

