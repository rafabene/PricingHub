package com.rafabene.processador.processapedidos;

import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.rafabene.processador.dominio.vo.OrdemCompra;

import org.apache.kafka.common.serialization.Serializer;

public class OrdemCompraSerializer implements Serializer<OrdemCompra>{

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Jsonb jsonb = JsonbBuilder.create();

    @Override
    public byte[] serialize(String topic, OrdemCompra ordemCompra) {
        logger.fine("Serializing " + ordemCompra);
        String json = jsonb.toJson(ordemCompra);
        logger.fine("JSON resultante: " + json);
        return json.getBytes(Charset.forName("UTF-8"));
    }

    
}
