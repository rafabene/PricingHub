package com.rafabene.processador.kafka;

import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.rafabene.processador.dominio.vo.OrdemCompra;

import org.apache.kafka.common.serialization.Deserializer;

public class OrdemCompraDeserializer implements Deserializer<OrdemCompra>{


    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Jsonb jsonb = JsonbBuilder.create();
    
    @Override
    public OrdemCompra deserialize(String topic, byte[] data) {
        String json = new String(data, Charset.forName("UTF-8"));
        logger.fine("Recebendo JSON: " + json);
        OrdemCompra ordemCompra = jsonb.fromJson(json, OrdemCompra.class);
        logger.fine("JSON convertido para Objeto: " + ordemCompra);
        return ordemCompra;
    }
    
}
