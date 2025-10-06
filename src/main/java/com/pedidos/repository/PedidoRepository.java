package com.pedidos.repository;

import com.pedidos.model.Pedido;
import com.pedidos.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findByClienteId(UUID clienteId);

    List<Pedido> findByStatus(StatusPedido status);
}
