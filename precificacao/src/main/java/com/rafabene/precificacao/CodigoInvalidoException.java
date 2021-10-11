package com.rafabene.precificacao;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class CodigoInvalidoException extends WebApplicationException {

    public CodigoInvalidoException(String mensagem){
        super(Response.status(Response.Status.NOT_ACCEPTABLE).entity(mensagem).type("text/plain").build());
    }

    
}
