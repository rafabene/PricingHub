package com.rafabene;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

@Path("volume/{cliente}")
public class VolumeFinanceiroRest {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getVolumeFinanceiro(@PathParam("cliente") String cliente){
        NamedCache<String, Double> volumePorCliente = CacheFactory.getCache("valorAcumulado");
        Double volume = volumePorCliente.get(cliente);
        if (volume == null){
            return Response.status(Status.NOT_FOUND).entity("Sem valor para o cliente " + cliente).build();
        }else{
            return Response.ok(volume).build();
        }
    }
    
}
