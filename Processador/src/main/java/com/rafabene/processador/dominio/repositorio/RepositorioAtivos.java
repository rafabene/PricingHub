package com.rafabene.processador.dominio.repositorio;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import com.rafabene.processador.dominio.entidade.Ativo;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.EqualsFilter;

@ApplicationScoped
public class RepositorioAtivos {

    private NamedCache<Long, Ativo> ativosEmMemoria = CacheFactory.getCache("ativos");

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public Ativo obtemAtivoPorNome(String nome) {
        Ativo ativo = null;
        Filter<String> filter = new EqualsFilter<String, String>("getNome", nome);
        for (Iterator<Entry<Long, Ativo>> iter = ativosEmMemoria.entrySet(filter).iterator(); iter.hasNext();) {
            Map.Entry<Long,Ativo> entry = iter.next();
            logger.info(entry.getValue().toString());
            ativo = (Ativo) entry.getValue();
        }
        return ativo;

    }

}
