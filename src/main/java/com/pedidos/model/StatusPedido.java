package com.pedidos.model;

public enum StatusPedido {
    PENDENTE,
    PAGO,
    ENVIADO,
    CONFIRMADO, // <--- Adicionado para resolver erros como "cannot find symbol CONFIRMADO"
    CONCLUIDO,
    CANCELADO
}

