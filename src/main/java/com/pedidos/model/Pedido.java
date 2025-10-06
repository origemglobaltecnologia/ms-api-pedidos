package com.pedidos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    private UUID id;

    private UUID clienteId;

    @ElementCollection
    @CollectionTable(name = "pedido_produtos", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<ProdutoPedido> produtos = new ArrayList<>();

    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    public Pedido() {
        this.id = UUID.randomUUID(); // CORREÇÃO: Inicializa o ID com um UUID
    }

    public Pedido(UUID clienteId, List<ProdutoPedido> produtos, LocalDateTime dataPedido, StatusPedido status) {
        this(); // Chama o construtor padrão para gerar o ID
        this.clienteId = clienteId;
        this.produtos = produtos;
        this.dataPedido = dataPedido;
        this.status = status;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getClienteId() { return clienteId; }
    public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }

    public List<ProdutoPedido> getProdutos() { return produtos; }
    public void setProdutos(List<ProdutoPedido> produtos) { this.produtos = produtos; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
}

