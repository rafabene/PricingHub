package com.rafabene.processador.dominio.entidade;

import java.io.Serializable;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Ativo)) {
            return false;
        }
        Ativo ativo = (Ativo) o;
        return Objects.equals(id, ativo.id) && Objects.equals(name, ativo.name) && Objects.equals(tipoPrecificacao, ativo.tipoPrecificacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tipoPrecificacao);
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
