package com.pedidos.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PedidoTest {

    @Test
    void deveCriarPedidoComProdutosECliente() {
        UUID clienteId = UUID.randomUUID();
        UUID produto1 = UUID.randomUUID();
        UUID produto2 = UUID.randomUUID();

        ProdutoPedido p1 = new ProdutoPedido(produto1, 2);
        ProdutoPedido p2 = new ProdutoPedido(produto2, 1);

        List<ProdutoPedido> produtos = Arrays.asList(p1, p2);

        Pedido pedido = new Pedido(
                clienteId,
                produtos,
                LocalDateTime.now(),
                StatusPedido.PENDENTE
        );

        assertNotNull(pedido.getId(), "O ID do pedido deve ser gerado (UUID).");
        assertEquals(clienteId, pedido.getClienteId(), "O cliente deve ser o mesmo informado.");
        assertEquals(2, pedido.getProdutos().size(), "Deve conter dois produtos.");
        assertEquals(StatusPedido.PENDENTE, pedido.getStatus(), "O status inicial deve ser PENDENTE.");
    }

    @Test
    void deveAtualizarStatusDoPedido() {
        Pedido pedido = new Pedido(
                UUID.randomUUID(),
                new ArrayList<>(),
                LocalDateTime.now(),
                StatusPedido.PENDENTE
        );

        pedido.setStatus(StatusPedido.CONFIRMADO);

        assertEquals(StatusPedido.CONFIRMADO, pedido.getStatus(), "O status deve ser atualizado para CONFIRMADO.");
    }

    @Test
    void deveAdicionarProdutoAoPedido() {
        Pedido pedido = new Pedido(
                UUID.randomUUID(),
                new ArrayList<>(),
                LocalDateTime.now(),
                StatusPedido.PENDENTE
        );

        UUID produtoId = UUID.randomUUID();
        ProdutoPedido produto = new ProdutoPedido(produtoId, 3);

        pedido.getProdutos().add(produto);

        assertEquals(1, pedido.getProdutos().size(), "O pedido deve conter um produto.");
        assertEquals(3, pedido.getProdutos().get(0).getQuantidade(), "A quantidade do produto deve ser 3.");
    }
}
