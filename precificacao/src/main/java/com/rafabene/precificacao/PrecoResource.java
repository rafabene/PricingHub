package com.rafabene.precificacao;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/preco")
@RequestScoped
public class PrecoResource {

    @Inject
    private RepositorioPreco repositorioPreco;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Path("/{codigo}")
    @PUT
    public void atualizaPreco(@PathParam("codigo") String codigoAtivo, Double preco){
        validaCodigo(codigoAtivo);
        repositorioPreco.atualizaPreco(codigoAtivo, preco);
    }

    @GET
    @Path("/{codigo}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Double getPreco(@PathParam("codigo") String codigoAtivo,@HeaderParam("token")  String token){
        validaToken(token);
        validaCodigo(codigoAtivo);
        return repositorioPreco.getPreco(codigoAtivo);

    }

    private void validaToken(String token) {
        if (token == null || token.isEmpty()){
            throw new TokenInvalidoException(token);
        }
        //TODO Definir quais as regras de validação do Token
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
                throw new TokenInvalidoException(codigoAtivo);
        }
    }

    
}
