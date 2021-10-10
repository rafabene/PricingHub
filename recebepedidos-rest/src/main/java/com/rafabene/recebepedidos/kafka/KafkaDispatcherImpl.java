package com.rafabene.recebepedidos.kafka;

import java.util.concurrent.ExecutionException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.rafabene.recebepedidos.rest.OrdemCompra;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

@RequestScoped
public class KafkaDispatcherImpl implements KafkaService {

    @Inject
    private KafkaProducer<String, OrdemCompra> producer;

    public void enviarOrdemCompra(OrdemCompra ordemCompra) throws InterruptedException, ExecutionException{
        producer.send(
            new ProducerRecord<String,OrdemCompra>("ordemCompra", ordemCompra)
        ).get();
    }
    
}
