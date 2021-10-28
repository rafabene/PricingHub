package com.rafabene.alarme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

@Path("alarmes")
public class AlarmesRest {

    public NamedCache<String, Alarme> alarmes = CacheFactory.getCache("alarmes");

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAlarmes(){
        StringBuilder sb = new StringBuilder();
        for(String chave: alarmes.keySet()){
            Alarme alarme = alarmes.get(chave);
            sb.append(alarme.getMensagem() + "\n");
        }
        return sb.toString();
    }
    
}
