package com.rafabene.agendamento.kafka;

import java.util.List;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.rafabene.pedido.dominio.vo.Pedido;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class KafkaCDIProducer {
    
    @Inject
    @ConfigProperty(name = "pedido.brokerServers")
    private String brokerServers;

    @Inject
    @ConfigProperty(name = "agendamentoPedidos.topic")
    private String agendamentosTopic;

    @Produces
    public KafkaConsumer<String, Pedido> getKafkaConsumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerServers);
        properties.put("group.id", "agendamento");
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", PedidoDeserializer.class);
        KafkaConsumer<String, Pedido> consumer = new KafkaConsumer<String, Pedido>(properties);
        consumer.subscribe(List.of(agendamentosTopic));
        return consumer;
    }
}
