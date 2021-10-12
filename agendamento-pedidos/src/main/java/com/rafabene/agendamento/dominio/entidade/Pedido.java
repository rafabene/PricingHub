package com.rafabene.agendamento.dominio.entidade;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Pedido implements Serializable {

    private UUID id;

    private Ativo ativo;

    private int quantidadeAtivos;

    private Double preco;

    private Double precoTotal;

    public Pedido(Ativo ativo, int quantidadeAtivos, Double preco) {
        this.id = UUID.randomUUID();
        this.ativo = ativo;
        this.quantidadeAtivos = quantidadeAtivos;
        this.preco = preco;
        this.precoTotal = preco * quantidadeAtivos;
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


    public Double getPreco() {
        return this.preco;
    }


    public Double getPrecoTotal() {
        return this.precoTotal;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Pedido)) {
            return false;
        }
        Pedido pedido = (Pedido) o;
        return Objects.equals(ativo, pedido.ativo) && quantidadeAtivos == pedido.quantidadeAtivos && Objects.equals(preco, pedido.preco) && Objects.equals(precoTotal, pedido.precoTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ativo, quantidadeAtivos, preco, precoTotal);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", quantidadeAtivos='" + getQuantidadeAtivos() + "'" +
            ", preco='" + getPreco() + "'" +
            ", precoTotal='" + getPrecoTotal() + "'" +
            "}";
    }


    
}
