package com.rafabene.precificacao;

public interface RepositorioPreco {

    public void atualizaPreco(String codigo, Double preco);

    public Double getPreco(String codigo);
    
}
