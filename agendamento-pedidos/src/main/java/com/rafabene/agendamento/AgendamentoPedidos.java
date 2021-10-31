package com.rafabene.agendamento;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.rafabene.pedido.dominio.vo.Pedido;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class AgendamentoPedidos {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private NamedCache<String,Set<Pedido>> pedidosPorClienteMemoria = CacheFactory.getCache("pedidos");

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
        Set<Pedido> pedidosPorCliente = pedidosPorClienteMemoria.get(pedido.getTokenCliente());
        if (pedidosPorCliente == null){
            pedidosPorCliente = new HashSet<Pedido>();
        }
        pedidosPorCliente.add(pedido);
        pedidosPorClienteMemoria.put(pedido.getTokenCliente(), pedidosPorCliente);
        logger.fine(pedidosPorCliente.toString());
    }



}
