package com.rafabene.processador.dominio.repositorio;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;

import com.rafabene.processador.dominio.entidade.Ativo;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.EqualsFilter;

@ApplicationScoped
public class RepositorioAtivos {

    private NamedCache<Long, Ativo> ativosEmMemoria = CacheFactory.getCache("ativos");

    /**
     * 
     * @param nome
     * @return Primeira ocorrencia do Ativo com o nome enviado como parâmetro.
     */
    public Ativo obtemAtivoPorNome(String nome) {
        Filter<String> filter = new EqualsFilter<String, String>("getNome", nome);
        for (Iterator<Entry<Long, Ativo>> iter = ativosEmMemoria.entrySet(filter).iterator(); iter.hasNext();) {
            Map.Entry<Long, Ativo> entry = iter.next();
            // Retorna a primeira ocorrência
            return (Ativo) entry.getValue();
        }
        // Se não encontrar, retorna nulo
        return null;
    }

    public void adicionar(Ativo ativo) {
        ativosEmMemoria.put(ativo.getId(), ativo);
    }

}
