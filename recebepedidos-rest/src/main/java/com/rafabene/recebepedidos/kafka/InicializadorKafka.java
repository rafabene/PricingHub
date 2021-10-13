package com.rafabene.recebepedidos.kafka;

import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.rafabene.recebepedidos.dominio.vo.OrdemCompra;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.helidon.config.Config;
import io.helidon.microprofile.cdi.RuntimeStart;

/**
 * 
 * Kafka Producer para enviar a ordem de compra
 */
@ApplicationScoped
public class InicializadorKafka {


    @Inject
    @ConfigProperty(name="kafka.bootstrapServers")
    private String bootstrapServers;

    private KafkaProducer<String, OrdemCompra> producer;

    public void startKafka(@Observes @RuntimeStart Config config){
        //Força o inicio do Kafka para conectar no start da aplicação
        getKafkaProducer();
    }


    @Produces
    private KafkaProducer<String, OrdemCompra> getKafkaProducer() {
        if (producer == null){
            Properties kafkaProps = new Properties();
            kafkaProps.put("bootstrap.servers", bootstrapServers);
            kafkaProps.put("key.serializer",StringSerializer.class);
            kafkaProps.put("value.serializer",OrdemCompraSerializer.class);
            producer = new KafkaProducer<String, OrdemCompra>(kafkaProps);
        }
        return producer;
    }
    
}
