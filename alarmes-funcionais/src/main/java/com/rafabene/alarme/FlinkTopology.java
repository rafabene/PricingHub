package com.rafabene.alarme;

import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.rafabene.alarme.flink.AggregatePedidos;
import com.rafabene.alarme.flink.AlarmeSink;
import com.rafabene.alarme.flink.PedidosSource;
import com.rafabene.alarme.flink.ProcessaAlarme;
import com.rafabene.pedido.dominio.vo.Pedido;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class FlinkTopology {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    @Inject
    @ConfigProperty(name = "janelaTempoSegundos")
    private Integer janelaTempoSegundos;

    @Inject
    @ConfigProperty(name = "limiteValorAlarme")
    private Integer limiteValorAlarme;

    public void setup(@Observes @RuntimeStart Config config) {
        logger.info("Configurando Flink Topology");
        try {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            PedidosSource cs = new PedidosSource();
            DataStreamSource<Pedido> pedidosStream = env.addSource(cs);
            pedidosStream.name("Pedidos");

            WatermarkStrategy<Pedido> watermarkStrategy = WatermarkStrategy.<Pedido>forMonotonousTimestamps()
                    .withTimestampAssigner(
                        (pedido, timestamp) -> pedido.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli()
                    );

            DataStream<Alarme> alarmes = pedidosStream
                    .assignTimestampsAndWatermarks(watermarkStrategy)
                    .keyBy(key -> key.getTokenCliente())
                    .window(SlidingEventTimeWindows
                            .of(Time.seconds(janelaTempoSegundos), 
                                Time.seconds(1)))
                    .aggregate(
                        new AggregatePedidos(), 
                        new ProcessaAlarme(limiteValorAlarme, janelaTempoSegundos)
                    ).name("Processa Alarmes");
            
            alarmes.addSink(new AlarmeSink())
                    .name("Dispara alarme");

            env.execute("Alarme");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

}
