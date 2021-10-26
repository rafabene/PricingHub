package com.rafabene.alarme.flink;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import com.rafabene.pedido.dominio.vo.Pedido;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.MapListener;
import com.tangosol.util.listener.SimpleMapListener;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

public class PedidosSource extends RichSourceFunction<Pedido> {

    private volatile boolean isRunning = true;
    private transient ConcurrentLinkedQueue<Pedido> pedidos;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        this.pedidos = new ConcurrentLinkedQueue<>();
        NamedCache<String, Set<Pedido>> nc = CacheFactory.getCache("pedidos");
        MapListener<String, Set<Pedido>> sml = new SimpleMapListener<String, Set<Pedido>>()
                .addUpdateHandler((event) -> {
                    // Evita que pedidos iguais sejam processados
                    if (!event.getNewValue().equals(event.getOldValue())) {
                        // Pega somente Pedidos que foram alterados
                        event.getNewValue().removeAll(event.getOldValue());
                        this.pedidos.addAll(event.getNewValue());
                    }
                }).addInsertHandler((event) -> {
                    this.pedidos.addAll(event.getNewValue());
                });
        nc.addMapListener(sml);
    }

    @Override
    public void run(SourceContext<Pedido> ctx) throws Exception {
        Logger.getLogger(this.getClass().toString()).info("Coletando Pedidos");
        while(isRunning){
            while (!pedidos.isEmpty()){
                synchronized(ctx.getCheckpointLock()){
                    ctx.collect(pedidos.poll());
                }
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }

}
