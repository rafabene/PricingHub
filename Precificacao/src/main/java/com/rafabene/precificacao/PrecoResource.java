package com.rafabene.precificacao;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("/preco")
@RequestScoped
public class PrecoResource {

    @Inject
    private RepositorioPreco repositorioPreco;

    @Path("/{codigo}")
    @PUT
    public void atualizaPreco(@PathParam("codigo") String codigoAtivo, Double preco){
        validaCodigo(codigoAtivo);
        repositorioPreco.atualizaPreco(codigoAtivo, preco);
    }

    @GET
    @Path("/{codigo}")
    @Produces("text/plain")
    public Double getPreco(@PathParam("codigo") String codigoAtivo){
        validaCodigo(codigoAtivo);
        return repositorioPreco.getPreco(codigoAtivo);

    }

    private void validaCodigo(String codigoAtivo) {
        switch (codigoAtivo) {
            case "MB":
            case "B":
            case "N":
            case "C":
            case "MA":
                break;
            default:
                throw new CodigoInvalidoException(String.format("Código '%s' não é válido", codigoAtivo));
        }
    }

    
}
