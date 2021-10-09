package com.rafabene.processador.recebePedidos;

import javax.json.bind.JsonbException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ProcessionExceptionMapper implements ExceptionMapper<ProcessingException>{

    @Override
    public Response toResponse(ProcessingException exception) {
        if (exception.getCause() instanceof JsonbException){
            return Response.status(Status.NOT_ACCEPTABLE).entity(
                exception.getMessage() + " " + exception.getCause().getMessage())
                .build();
        } else return Response.status(Status.NOT_ACCEPTABLE).entity(exception.getMessage()).build();
    }
    
}
