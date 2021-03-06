package com.rafabene.precificacao;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class CodigoInvalidoException extends WebApplicationException {

    public CodigoInvalidoException(String codigoAtivo) {
        super(Response.status(Response.Status.NOT_ACCEPTABLE)
                .entity(String.format("Código '%s' não é válido", codigoAtivo)).type("text/plain").build());
    }

}
