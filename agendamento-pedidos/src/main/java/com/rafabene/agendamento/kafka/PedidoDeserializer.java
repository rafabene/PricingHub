package com.rafabene.agendamento.kafka;

import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.rafabene.agendamento.dominio.vo.Pedido;

import org.apache.kafka.common.serialization.Deserializer;

public class PedidoDeserializer implements Deserializer<Pedido> {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Jsonb jsonb = JsonbBuilder.create();

    @Override
    public Pedido deserialize(String topic, byte[] data) {
        String json = new String(data, Charset.forName("UTF-8"));
        logger.fine("Recebendo JSON: " + json);
        Pedido pedido = jsonb.fromJson(json, Pedido.class);
        logger.fine("JSON convertido para Objeto: " + pedido);
        return pedido;
    }

}
