package com.rafabene.recebepedidos.kafka;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.rafabene.recebepedidos.dominio.vo.OrdemCompra;

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
        ProducerRecord<String, OrdemCompra> producerRecord = new ProducerRecord<String,OrdemCompra>(ordemTopic, ordemCompra.getTokenCliente(), ordemCompra);
        producer.send(producerRecord).get();
    }
    
}
