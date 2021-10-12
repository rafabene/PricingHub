package com.rafabene.agendamento.dominio.vo;

import java.io.Serializable;
import java.util.UUID;

public class Pedido implements Serializable {

    private UUID id;

    private String tokenCliente;

    private Ativo ativo;

    private int quantidadeAtivos;

    private Double precoUnitario;

    private Double precoTotal;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTokenCliente() {
        return tokenCliente;
    }

    public void setTokenCliente(String tokenCliente) {
        this.tokenCliente = tokenCliente;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }

    public int getQuantidadeAtivos() {
        return quantidadeAtivos;
    }

    public void setQuantidadeAtivos(int quantidadeAtivos) {
        this.quantidadeAtivos = quantidadeAtivos;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ativo == null) ? 0 : ativo.hashCode());
        result = prime * result + ((precoTotal == null) ? 0 : precoTotal.hashCode());
        result = prime * result + ((precoUnitario == null) ? 0 : precoUnitario.hashCode());
        result = prime * result + quantidadeAtivos;
        result = prime * result + ((tokenCliente == null) ? 0 : tokenCliente.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pedido other = (Pedido) obj;
        if (ativo == null) {
            if (other.ativo != null)
                return false;
        } else if (!ativo.equals(other.ativo))
            return false;
        if (precoTotal == null) {
            if (other.precoTotal != null)
                return false;
        } else if (!precoTotal.equals(other.precoTotal))
            return false;
        if (precoUnitario == null) {
            if (other.precoUnitario != null)
                return false;
        } else if (!precoUnitario.equals(other.precoUnitario))
            return false;
        if (quantidadeAtivos != other.quantidadeAtivos)
            return false;
        if (tokenCliente == null) {
            if (other.tokenCliente != null)
                return false;
        } else if (!tokenCliente.equals(other.tokenCliente))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pedido [ tokenCliente=" + tokenCliente + ", id=" + id + ", ativo=" + ativo +  ", precoTotal=" + precoTotal + ", precoUnitario="
                + precoUnitario + ", quantidadeAtivos=" + quantidadeAtivos + "]";
    }

}
