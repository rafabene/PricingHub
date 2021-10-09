package com.rafabene.recebepedidos.rest;

import java.util.logging.Logger;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/pedidos")
public class RecebePedidosResource {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response recebePedidos(@NotNull @Valid OrdemCompra ordemCompra) {
        logger.info(ordemCompra.toString());
        return Response.accepted().build();
    }
}
