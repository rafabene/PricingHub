package com.rafabene.precificacao.infra;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.helidon.microprofile.cdi.RuntimeStart;


/** 
    O cache é inicializado junto com a aplicação para verificação
    da configuração e evitar que o primeiro request seja lento
*/
@ApplicationScoped
public class StartupCache {

    @Inject
    @ConfigProperty(name = "carregaPrecoPadrao", defaultValue = "true")
    private boolean carregaPrecoPadrao;


    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void startCacheCluster(@Observes @RuntimeStart Object o){
        logger.info("Inicializando Coherence Cluster");
        NamedCache<String, Double> precos = CacheFactory.getCache("precos");
        if (carregaPrecoPadrao){
            logger.info("Pre-carregando preços padrão");
            precos.put("MB", 1.0);
            precos.put("B", 2.0);
            precos.put("N", 3.0);
            precos.put("C", 4.0);
            precos.put("MA", 5.0);
        }
    }
    
}
