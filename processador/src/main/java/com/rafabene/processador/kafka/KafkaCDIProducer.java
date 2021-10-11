package com.rafabene.processador.kafka;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.BeforeDestroyed;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.rafabene.processador.dominio.vo.OrdemCompra;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class KafkaCDIProducer {

    @Inject
    @ConfigProperty(name = "ordemcompra.brokerServers")
    private String brokerServers;

    private static Properties properties;

    private Logger logger = Logger.getLogger(this.getClass().getName());
    
    private Properties setup(){
        if (properties == null){
            logger.info("Configurando Kafka Producers and Consumers");
            properties = new Properties();
            properties.put("bootstrap.servers", brokerServers);
            properties.put("group.id", "princingHub");
            properties.put("key.serializer", StringSerializer.class);
            properties.put("key.deserializer", StringDeserializer.class);
            properties.put("value.serializer", OrdemCompraSerializer.class);
            properties.put("value.deserializer", OrdemCompraDeserializer.class);
        }
        return properties;
    }


    void tearDown(@Observes @BeforeDestroyed(ApplicationScoped.class) Object o){
        getKafkaConsumer().close();
        getKafkaProducer().close();
    }

    @Produces
    public KafkaConsumer<String, OrdemCompra> getKafkaConsumer() {
        setup();
        KafkaConsumer<String, OrdemCompra> consumer = new KafkaConsumer<String, OrdemCompra>(setup());
        consumer.subscribe(List.of("ordemCompra"));
        return consumer;
    }

    @Produces
    public KafkaProducer<String, OrdemCompra> getKafkaProducer() {
        KafkaProducer<String, OrdemCompra> producer = new KafkaProducer<String, OrdemCompra>(setup());
        return producer;
    }

}
