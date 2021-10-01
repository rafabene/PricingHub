package com.rafabene.precificacao.infra;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import com.tangosol.net.CacheFactory;

import io.helidon.microprofile.cdi.RuntimeStart;


/** 
    O cache é inicializado junto com a aplicação para verificação
    da configuração e evitar que o primeiro request seja lento
*/
@ApplicationScoped
public class StartupCache {


    public void startCacheCluster(@Observes @RuntimeStart Object o){
        CacheFactory.getCache("precos");
    }
    
}
