package com.rafabene.processador.dominio.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Pedido implements Serializable {

    private UUID id;

    private String tokenCliente;

    private Ativo ativo;

    private int quantidadeAtivos;

    private Double precoUnitario;

    private Double precoTotal;

    private LocalDateTime timestamp;

    public Pedido(String tokenCliente, Ativo ativo, int quantidadeAtivos, Double precoUnitario, LocalDateTime timestamp) {
        this.id = UUID.randomUUID();
        this.tokenCliente = tokenCliente;
        this.ativo = ativo;
        this.quantidadeAtivos = quantidadeAtivos;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoUnitario * quantidadeAtivos;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return this.id;
    }

    public Ativo getAtivo() {
        return this.ativo;
    }

    public int getQuantidadeAtivos() {
        return this.quantidadeAtivos;
    }


    public Double getPrecoUnitario() {
        return this.precoUnitario;
    }


    public Double getPrecoTotal() {
        return this.precoTotal;
    }

    public String getTokenCliente() {
        return tokenCliente;
    }

    public Pedido(UUID id, String tokenCliente, Ativo ativo, int quantidadeAtivos, Double precoUnitario, Double precoTotal, LocalDateTime timestamp) {
        this.id = id;
        this.tokenCliente = tokenCliente;
        this.ativo = ativo;
        this.quantidadeAtivos = quantidadeAtivos;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoTotal;
        this.timestamp = timestamp;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setTokenCliente(String tokenCliente) {
        this.tokenCliente = tokenCliente;
    }
    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }
    public void setQuantidadeAtivos(int quantidadeAtivos) {
        this.quantidadeAtivos = quantidadeAtivos;
    }
    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Pedido)) {
            return false;
        }
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id) && Objects.equals(tokenCliente, pedido.tokenCliente) && Objects.equals(ativo, pedido.ativo) && quantidadeAtivos == pedido.quantidadeAtivos && Objects.equals(precoUnitario, pedido.precoUnitario) && Objects.equals(precoTotal, pedido.precoTotal) && Objects.equals(timestamp, pedido.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenCliente, ativo, quantidadeAtivos, precoUnitario, precoTotal, timestamp);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", tokenCliente='" + getTokenCliente() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", quantidadeAtivos='" + getQuantidadeAtivos() + "'" +
            ", precoUnitario='" + getPrecoUnitario() + "'" +
            ", precoTotal='" + getPrecoTotal() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }

    
}
