package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransferenciaRequest {

    @NotNull(message = "ID de origem é obrigatório")
    private Long fromId;

    @NotNull(message = "ID de destino é obrigatório")
    private Long toId;

    @NotNull(message = "Valor da transferência é obrigatório")
    @Positive(message = "Valor da transferência deve ser positivo")
    private BigDecimal amount;

    // Constructors
    public TransferenciaRequest() {
    }

    public TransferenciaRequest(Long fromId, Long toId, BigDecimal amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}






