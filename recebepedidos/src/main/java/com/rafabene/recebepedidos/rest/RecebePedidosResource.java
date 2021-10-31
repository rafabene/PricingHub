package com.rafabene.recebepedidos.rest;

import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.rafabene.recebepedidos.dominio.vo.InputAPI;
import com.rafabene.recebepedidos.kafka.KafkaService;

import org.eclipse.microprofile.faulttolerance.Timeout;


@Path("/pedidos")
public class RecebePedidosResource {


    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private KafkaService kafkaService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.MILLIS, value = 500) //Failfast
    public Response recebePedidos(@NotNull @Valid InputAPI inputAPI) {
        logger.fine(inputAPI.toString());
        try {
            switch(inputAPI.getVersao()){
                //API Versão 1
                case V1:
                    kafkaService.enviarOrdemCompra(inputAPI.getOrdemCompra());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.accepted().build();
    }
}
