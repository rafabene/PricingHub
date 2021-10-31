package com.rafabene.recebepedidos.dominio.vo;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class InputAPI {

    @NotNull
    private VERSAO versao;

    @NotNull
    @Valid
    private OrdemCompra ordemCompra;


    public InputAPI() {
    }

    public InputAPI(VERSAO versao, OrdemCompra ordemCompra) {
        this.versao = versao;
        this.ordemCompra = ordemCompra;
    }

    public VERSAO getVersao() {
        return this.versao;
    }

    public void setVersao(VERSAO versao) {
        this.versao = versao;
    }

    public OrdemCompra getOrdemCompra() {
        return this.ordemCompra;
    }

    public void setOrdemCompra(OrdemCompra ordemCompra) {
        this.ordemCompra = ordemCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof InputAPI)) {
            return false;
        }
        InputAPI inputAPI = (InputAPI) o;
        return Objects.equals(versao, inputAPI.versao) && Objects.equals(ordemCompra, inputAPI.ordemCompra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versao, ordemCompra);
    }

    @Override
    public String toString() {
        return "{" +
            " versao='" + getVersao() + "'" +
            ", ordemCompra='" + getOrdemCompra() + "'" +
            "}";
    }

    
}