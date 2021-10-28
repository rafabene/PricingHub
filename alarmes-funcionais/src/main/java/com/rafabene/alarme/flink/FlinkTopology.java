package com.rafabene.alarme.flink;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.rafabene.alarme.Alarme;
import com.rafabene.alarme.flink.functions.AggregatePedidos;
import com.rafabene.alarme.flink.functions.FunctionMovimentacaoFinanceira;
import com.rafabene.alarme.flink.functions.ProcessaAlarme;
import com.rafabene.alarme.flink.sink.AlarmeSink;
import com.rafabene.alarme.flink.sink.MovimentacaoSink;
import com.rafabene.alarme.flink.source.PedidosSource;
import com.rafabene.pedido.dominio.vo.Pedido;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
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
        // Configurando o Flink em outra Thread para não bloquear o start do servidor
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
                    SingleOutputStreamOperator<Pedido> pedidosStream = env
                        .addSource(new PedidosSource())
                        .name("Pedidos");
        
                    /**
                    WatermarkStrategy<Pedido> watermarkStrategy = WatermarkStrategy.<Pedido>forBoundedOutOfOrderness(Duration.ofSeconds(1))
                            .withTimestampAssigner(
                                (pedido, timestamp) -> pedido.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli()
                            ).withIdleness(Duration.ofSeconds(1));
                    */
                    
                    DataStream<Alarme> alarmes = pedidosStream
                       // .assignTimestampsAndWatermarks(watermarkStrategy)
                        .keyBy(pedido -> pedido.getTokenCliente())
                        .window(SlidingProcessingTimeWindows
//                        .window(SlidingEventTimeWindows
                                .of(Time.seconds(janelaTempoSegundos), 
                                    Time.seconds(5)))
                        .aggregate(
                            new AggregatePedidos(), 
                            new ProcessaAlarme(limiteValorAlarme, janelaTempoSegundos)
                        ).name("Processa Alarmes");
                        
                        alarmes
                            .addSink(new AlarmeSink())
                            .name("Sink dos Alarmes");
                        

                    DataStream<Tuple2<String, Double>> volumeNegociacao = pedidosStream
                        .keyBy(pedido -> pedido.getTokenCliente())
                        .process(new FunctionMovimentacaoFinanceira())
                        .name("Processa Movimentação Financeira");
                        
                    volumeNegociacao
                        .addSink(new MovimentacaoSink())
                        .name("Sink da Movimentação");
        
                    env.execute("Alarme");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }).start();
    }

}
