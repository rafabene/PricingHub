package com.rafabene.precificacao.infra;

import javax.enterprise.context.RequestScoped;

import com.rafabene.precificacao.RepositorioPreco;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

@RequestScoped
public class PrecoDatagrid implements RepositorioPreco{

    private NamedCache<String, Double> precos = CacheFactory.getCache("precos");

    @Override
    public void atualizaPreco(String codigo, Double preco) {
        precos.put(codigo, preco);
    }

    @Override
    public Double getPreco(String codigo) {
        return precos.get(codigo);
    }
    
}
