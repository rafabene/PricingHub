package com.rafabene.alarme;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import com.rafabene.alarme.flink.PedidosSource;
import com.rafabene.pedido.dominio.vo.Pedido;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class FlinkTopology {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    public void setup(@Observes @RuntimeStart Object o) {
        logger.info("Configurando Flink Topology");
        try {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            PedidosSource cs = new PedidosSource();
            DataStreamSource<Pedido> pedidosStream = env.addSource(cs);
            pedidosStream.name("Pedidos");

            DataStream<Alarme> alarmes = pedidosStream
                .keyBy(key -> key.getTokenCliente())
                .process(new ProcessaPedidos())
                .name("processa pedidos");

            alarmes
                .addSink(new AlarmeSink())
                .name("alarme");

            env.execute("Alarme");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

}
