package com.rafabene.processador.dominio.entidade;

import java.io.Serializable;

public class Ativo implements Serializable {

    private Long id;

    private String name;

    private TipoPrecificacao tipoPrecificacao;

    public Ativo(Long id, String name, TipoPrecificacao tipoPrecificacao) {
        this.id = id;
        this.name = name;
        this.tipoPrecificacao = tipoPrecificacao;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TipoPrecificacao getTipoPrecificacao() {
        return tipoPrecificacao;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", tipoPrecificacao='" + getTipoPrecificacao() + "'" +
            "}";
    }

}
