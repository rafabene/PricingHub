package com.rafabene.processador.infocadastrais;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface Ativos {

    @GET
    @Path("/ativos")
    public File getAtivos();
    
}
