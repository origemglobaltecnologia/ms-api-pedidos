package com.pedidos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedidos.model.Pedido;
import com.pedidos.model.ProdutoPedido;
import com.pedidos.model.StatusPedido;
import com.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido;
    private UUID clienteId;
    private UUID produtoId;

    @BeforeEach
    void setup() {
        clienteId = UUID.randomUUID();
        produtoId = UUID.randomUUID();
        ProdutoPedido produto = new ProdutoPedido(produtoId, 2);
        pedido = new Pedido(clienteId, List.of(produto), LocalDateTime.now(), StatusPedido.PENDENTE);
    }

    @Test
    void deveListarPedidos() throws Exception {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value(clienteId.toString()));
    }

    @Test
    void deveBuscarPedidoPorId() throws Exception {
        UUID id = UUID.randomUUID();
        pedido.setId(id);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/pedidos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(clienteId.toString()));
    }

    @Test
    void deveCriarPedido() throws Exception {
        Map<String, Object> dados = new HashMap<>();
        dados.put("clienteId", clienteId.toString());
        List<Map<String, Object>> produtos = List.of(
                Map.of("produtoId", produtoId.toString(), "quantidade", 2)
        );
        dados.put("produtos", produtos);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(clienteId.toString()));
    }

    @Test
    void deveAtualizarPedido() throws Exception {
        UUID id = UUID.randomUUID();
        pedido.setId(id);
        pedido.setStatus(StatusPedido.PENDENTE);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Map<String, Object> update = Map.of("status", "CONFIRMADO");

        mockMvc.perform(put("/pedidos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void deveExcluirPedido() throws Exception {
        UUID id = UUID.randomUUID();
        when(pedidoRepository.existsById(id)).thenReturn(true);

        mockMvc.perform(delete("/pedidos/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveListarProdutosViaAPIExterna() throws Exception {
        when(restTemplate.getForEntity("http://localhost:8081/api/produtos", String.class))
                .thenReturn(ResponseEntity.ok("[{\"nome\":\"Produto Teste\"}]"));

        mockMvc.perform(get("/pedidos/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"nome\":\"Produto Teste\"}]"));
    }

    @Test
    void deveListarClientesViaAPIExterna() throws Exception {
        when(restTemplate.getForEntity("http://localhost:8082/clientes", String.class))
                .thenReturn(ResponseEntity.ok("[{\"nome\":\"Cliente Teste\"}]"));

        mockMvc.perform(get("/pedidos/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"nome\":\"Cliente Teste\"}]"));
    }
}
