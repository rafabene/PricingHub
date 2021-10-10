package com.rafabene.processador.dominio.vo;

public class OrdemCompra {

    private String nomeAtivo;

    private int quantidade;

    private String tokenCliente;

    public String getNomeAtivo() {
        return nomeAtivo;
    }

    public void setNomeAtivo(String nomeAtivo) {
        this.nomeAtivo = nomeAtivo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getTokenCliente() {
        return tokenCliente;
    }

    public void setTokenCliente(String tokenCliente) {
        this.tokenCliente = tokenCliente;
    }

    @Override
    public String toString() {
        return "OrdemCompra [nomeAtivo=" + nomeAtivo + ", quantidade=" + quantidade + ", tokenCliente=" + tokenCliente
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nomeAtivo == null) ? 0 : nomeAtivo.hashCode());
        result = prime * result + quantidade;
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
        OrdemCompra other = (OrdemCompra) obj;
        if (nomeAtivo == null) {
            if (other.nomeAtivo != null)
                return false;
        } else if (!nomeAtivo.equals(other.nomeAtivo))
            return false;
        if (quantidade != other.quantidade)
            return false;
        if (tokenCliente == null) {
            if (other.tokenCliente != null)
                return false;
        } else if (!tokenCliente.equals(other.tokenCliente))
            return false;
        return true;
    }

    

   

}
