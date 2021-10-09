package com.rafabene.recebepedidos.rest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class OrdemCompra {

    @NotBlank
    private String nomeAtivo;

    @Min(1)
    private int quantidade;

    @NotBlank
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

   

}
