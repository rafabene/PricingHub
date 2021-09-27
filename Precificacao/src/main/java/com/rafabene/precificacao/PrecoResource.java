package com.rafabene.precificacao;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


@Path("/preco")
@RequestScoped
public class PrecoResource {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private RepositorioPreco repositorioPreco;

    @Path("/{codigo}")
    @PUT
    public void atualizaPreco(@PathParam("codigo") String codigo, Double preco){
        logger.info("Codigo: "+ codigo);
        logger.info("Preço: "+ preco);
        switch (codigo) {
            case "MB":
            case "B":
            case "N":
            case "C":
            case "MA":
                repositorioPreco.atualizaPreco(codigo, preco);
                break;
            default:
                throw new WebApplicationException(
                    String.format("Código '%s' não é válido", codigo),
                    Response.Status.NOT_ACCEPTABLE);
        }
    }
    
}
