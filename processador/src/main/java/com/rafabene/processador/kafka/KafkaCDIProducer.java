package com.rafabene.processador.kafka;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.BeforeDestroyed;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.rafabene.processador.dominio.entidade.Pedido;
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

    @Inject
    @ConfigProperty(name = "ordemcompra.topic")
    private String ordemCompraTopic;

    private static Properties properties;

    private Logger logger = Logger.getLogger(this.getClass().getName());
    
    private Properties getBaseProperties(){
        if (properties == null){
            logger.info("Configurando Kafka Producers and Consumers");
            properties = new Properties();
            properties.put("bootstrap.servers", brokerServers);
            properties.put("group.id", "princingHub");
            properties.put("key.serializer", StringSerializer.class);
            properties.put("key.deserializer", StringDeserializer.class);
        }
        return properties;
    }


    void tearDown(@Observes @BeforeDestroyed(ApplicationScoped.class) Object o){
        getKafkaConsumerOrdem().close();
        getKafkaProducerOrdem().close();
        getKafkaProducerPedido().close();
    }

    @Produces
    public KafkaConsumer<String, OrdemCompra> getKafkaConsumerOrdem() {
        Properties p = getBaseProperties();
        p.put("value.deserializer", OrdemCompraDeserializer.class);
        KafkaConsumer<String, OrdemCompra> consumer = new KafkaConsumer<String, OrdemCompra>(p);
        consumer.subscribe(List.of(ordemCompraTopic));
        return consumer;
    }

    @Produces
    public KafkaProducer<String, OrdemCompra> getKafkaProducerOrdem() {
        Properties p = getBaseProperties();
        p.put("value.serializer", OrdemCompraSerializer.class);
        KafkaProducer<String, OrdemCompra> producer = new KafkaProducer<String, OrdemCompra>(p);
        return producer;
    }

    @Produces
    public KafkaProducer<String, Pedido> getKafkaProducerPedido() {
        Properties p = getBaseProperties();
        p.put("value.serializer", PedidoSerializer.class);
        KafkaProducer<String, Pedido> producer = new KafkaProducer<String, Pedido>(p);
        return producer;
    }


}
