package com.rafabene.processador.kafka;

import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.rafabene.processador.dominio.entidade.Pedido;

import org.apache.kafka.common.serialization.Serializer;

public class PedidoSerializer implements Serializer<Pedido>{

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Jsonb jsonb = JsonbBuilder.create();

    @Override
    public byte[] serialize(String topic, Pedido pedido) {
        logger.fine("Serializing " + pedido);
        String json = jsonb.toJson(pedido);
        logger.fine("JSON resultante: " + json);
        return json.getBytes(Charset.forName("UTF-8"));
    }

    
}
