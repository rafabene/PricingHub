package com.rafabene.processador.dominio.entidade;

import java.io.Serializable;
import java.util.UUID;

public class Pedido implements Serializable {

    private UUID id;

    private String tokenCliente;

    private Ativo ativo;

    private int quantidadeAtivos;

    private Double precoUnitario;

    private Double precoTotal;

    public Pedido(String tokenCliente, Ativo ativo, int quantidadeAtivos, Double precoUnitario) {
        this.id = UUID.randomUUID();
        this.tokenCliente = tokenCliente;
        this.ativo = ativo;
        this.quantidadeAtivos = quantidadeAtivos;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoUnitario * quantidadeAtivos;
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
        return "Pedido [ativo=" + ativo + ", id=" + id + ", precoTotal=" + precoTotal + ", precoUnitario="
                + precoUnitario + ", quantidadeAtivos=" + quantidadeAtivos + ", tokenCliente=" + tokenCliente + "]";
    }

    
}
