package com.rafabene.precificacao;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class TokenInvalidoException extends WebApplicationException {

    public TokenInvalidoException(String token) {
        super(Response.status(Response.Status.NOT_ACCEPTABLE)
                .entity(String.format("Token '%s' não é válido", token)).type("text/plain").build());
    }

}
