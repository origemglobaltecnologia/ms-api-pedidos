package com.pedidos.repository;

import com.pedidos.model.Pedido;
import com.pedidos.model.ProdutoPedido;
import com.pedidos.model.StatusPedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    void deveSalvarEBuscarPedidoPorCliente() {
        UUID clienteId = UUID.randomUUID();
        UUID produtoId = UUID.randomUUID();

        ProdutoPedido produto = new ProdutoPedido(produtoId, 2);
        Pedido pedido = new Pedido(
                clienteId,
                List.of(produto),
                LocalDateTime.now(),
                StatusPedido.PENDENTE
        );

        pedidoRepository.save(pedido);

        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);

        assertEquals(1, pedidos.size(), "Deve retornar um pedido para o cliente.");
        assertEquals(clienteId, pedidos.get(0).getClienteId(), "O cliente deve ser o mesmo informado.");
    }

    @Test
    void deveBuscarPedidosPorStatus() {
        UUID clienteId = UUID.randomUUID();

        Pedido pedido1 = new Pedido(clienteId, new ArrayList<>(), LocalDateTime.now(), StatusPedido.PENDENTE);
        Pedido pedido2 = new Pedido(clienteId, new ArrayList<>(), LocalDateTime.now(), StatusPedido.CONFIRMADO);

        pedidoRepository.saveAll(List.of(pedido1, pedido2));

        List<Pedido> pendentes = pedidoRepository.findByStatus(StatusPedido.PENDENTE);
        List<Pedido> confirmados = pedidoRepository.findByStatus(StatusPedido.CONFIRMADO);

        assertEquals(1, pendentes.size(), "Deve haver um pedido pendente.");
        assertEquals(1, confirmados.size(), "Deve haver um pedido confirmado.");
    }

    @Test
    void deveExcluirPedido() {
        UUID clienteId = UUID.randomUUID();
        Pedido pedido = new Pedido(clienteId, new ArrayList<>(), LocalDateTime.now(), StatusPedido.PENDENTE);

        Pedido salvo = pedidoRepository.save(pedido);
        UUID id = salvo.getId();

        pedidoRepository.deleteById(id);

        Optional<Pedido> resultado = pedidoRepository.findById(id);
        assertTrue(resultado.isEmpty(), "O pedido deve ser removido do repositório.");
    }
}
