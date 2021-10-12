package com.rafabene.agendamento;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.rafabene.agendamento.dominio.vo.Pedido;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class AgendamentoPedidos {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    NamedCache<String,Map<Integer,Pedido>> pedidosPorClienteMemoria = CacheFactory.getCache("pedidos");

    @Inject
    private KafkaConsumer<String, Pedido> consumerPedidos;

    public void agendarPedidos(@Observes @RuntimeStart Object o) {
        logger.info("Iniciando o agendamento de Pedidos");
        while(true){
            ConsumerRecords<String, Pedido> records  = consumerPedidos.poll(Duration.ofMillis(100));
            records.forEach(cr -> processaPedido(cr.value()));
        }

    }

    private void processaPedido(Pedido pedido) {
        Map<Integer,Pedido> pedidosPorCliente = pedidosPorClienteMemoria.get(pedido.getTokenCliente());
        if (pedidosPorCliente == null){
            pedidosPorCliente = new HashMap<>();
            pedidosPorClienteMemoria.put(pedido.getTokenCliente(), pedidosPorCliente);
        }
        pedidosPorCliente.put(pedido.hashCode(), pedido);
        logger.fine(pedidosPorCliente.toString());
    }



}
