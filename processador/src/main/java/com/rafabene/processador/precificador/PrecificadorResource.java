package com.rafabene.processador.precificador;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.rafabene.processador.dominio.entidade.TipoPrecificacao;

public interface PrecificadorResource {

    @GET
    @Path("/preco/{tipo}")
    @Produces("text/plain")
    public Double getPreco(@PathParam("tipo") TipoPrecificacao tipo);

}
