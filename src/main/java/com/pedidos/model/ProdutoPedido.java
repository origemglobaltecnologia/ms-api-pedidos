package com.pedidos.model;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class ProdutoPedido {

    private UUID produtoId;
    private Integer quantidade;

    public ProdutoPedido() {}

    public ProdutoPedido(UUID produtoId, Integer quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public UUID getProdutoId() { return produtoId; }
    public void setProdutoId(UUID produtoId) { this.produtoId = produtoId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
