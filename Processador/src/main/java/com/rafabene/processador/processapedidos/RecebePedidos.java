package com.rafabene.processador.processapedidos;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.rafabene.processador.dominio.vo.OrdemCompra;
import com.rafabene.processador.infocadastrais.ControleAtivosDia;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class RecebePedidos {

    @Inject
    @ConfigProperty(name = "ordemcompra.brokerServers")
    private String brokerServers;

    @Inject
    private ControleAtivosDia controleAtivosDia;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void inicializarRecebimentoPedidos(@Observes @RuntimeStart Object o) throws InterruptedException {
        
        // Aguardando download de Ativos
        while (true) {
            if (!controleAtivosDia.temPrecoDia()) {
                logger.info("Aguardando 10s o download dos ativos do dia.");
                Thread.sleep(10 * 1000);
            }
            break;
        }
        logger.info("Iniciando o processamento dos pedidos");
        obterPedidosKafka();
    }

    private void obterPedidosKafka() {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerServers);
        props.put("group.id", "princingHub");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", OrdemCompraDeserializer.class);
        KafkaConsumer<String, OrdemCompra> consumer = new KafkaConsumer<String, OrdemCompra>(props);
        consumer.subscribe(List.of("ordemCompra"));
        try {
            while (true) {
                ConsumerRecords<String, OrdemCompra> records = consumer.poll(Duration.ofSeconds(1));
                records.forEach(f -> logger.info(f.value().toString()));
            }
        } finally {
            consumer.close();
        }

    }

}