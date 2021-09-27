package com.rafabene.precificacao.infra;

import javax.enterprise.context.RequestScoped;

import com.rafabene.precificacao.RepositorioPreco;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

@RequestScoped
public class PrecoDatagrid implements RepositorioPreco{

    private static final String CACHE_NAME = "precos";

    @Override
    public void atualizaPreco(String codigo, Double preco) {
        NamedCache<Object, Object> precos = CacheFactory.getCache(CACHE_NAME);
        precos.put(codigo, preco);
    }
    
}
