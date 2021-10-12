package com.rafabene.agendamento;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import com.rafabene.agendamento.dominio.entidade.Pedido;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class AgendamentoPedidos {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @ConfigProperty(name = "pedido.brokerServers")
    private String brokerServers;

    public void agendarPedidos(@Observes @RuntimeStart Object o) {
        logger.info("Iniciando o agendamento de Pedidos");

    }

    private KafkaConsumer<String, Pedido> getKafkaConsumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerServers);
        properties.put("group.id", "princingHub");
        properties.put("key.deserializer", StringDeserializer.class);
        KafkaConsumer<String, Pedido> consumer = new KafkaConsumer<String, Pedido>(properties);
        consumer.subscribe(List.of("ordemCompra"));
        return consumer;
    }

}
