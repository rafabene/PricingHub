package com.rafabene.processador.infocadastrais;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public interface AtivosRestResource {

    @GET
    @Path("/ativos")
    @Produces("text/plain")
    public InputStream getAtivos(@HeaderParam("Authorization") String token);
    
}
