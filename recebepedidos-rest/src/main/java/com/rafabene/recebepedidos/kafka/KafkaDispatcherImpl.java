package com.rafabene.recebepedidos.kafka;

import java.util.concurrent.ExecutionException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.rafabene.recebepedidos.rest.OrdemCompra;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@RequestScoped
public class KafkaDispatcherImpl implements KafkaService {

    @Inject
    private KafkaProducer<String, OrdemCompra> producer;
    
    @Inject
    @ConfigProperty(name =  "kafka.ordemCompra.topic")
    private String ordemTopic;

    public void enviarOrdemCompra(OrdemCompra ordemCompra) throws InterruptedException, ExecutionException{
        producer.send(
            new ProducerRecord<String,OrdemCompra>(ordemTopic, ordemCompra)
        ).get();
    }
    
}
