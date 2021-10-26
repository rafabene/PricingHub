package com.rafabene.processador.dominio.vo;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class OrdemCompra {

    @NotBlank
    private String nomeAtivo;

    @Min(1)
    private int quantidade;

    @NotBlank
    private String tokenCliente;

    private LocalDateTime timestamp = LocalDateTime.now();

    public String getNomeAtivo() {
        return nomeAtivo;
    }

    public void setNomeAtivo(String nomeAtivo) {
        this.nomeAtivo = nomeAtivo.trim();
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
        this.tokenCliente = tokenCliente.trim();
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
        if (!(o instanceof OrdemCompra)) {
            return false;
        }
        OrdemCompra ordemCompra = (OrdemCompra) o;
        return Objects.equals(nomeAtivo, ordemCompra.nomeAtivo) && quantidade == ordemCompra.quantidade && Objects.equals(tokenCliente, ordemCompra.tokenCliente) && Objects.equals(timestamp, ordemCompra.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeAtivo, quantidade, tokenCliente, timestamp);
    }

    @Override
    public String toString() {
        return "{" +
            " nomeAtivo='" + getNomeAtivo() + "'" +
            ", quantidade='" + getQuantidade() + "'" +
            ", tokenCliente='" + getTokenCliente() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
   

}
