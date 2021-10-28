package com.rafabene.alarme.flink.functions;

import java.util.logging.Logger;

import com.rafabene.pedido.dominio.vo.Pedido;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class FunctionMovimentacaoFinanceira extends KeyedProcessFunction<String, Pedido, Tuple2<String, Double>> {

    private ValueState<Double> state;

    @Override
    public void open(Configuration parameters) throws Exception {
        state = getRuntimeContext().getState(new ValueStateDescriptor<>("valorAcumulado", Double.class));
    }

    @Override
    public void processElement(Pedido pedido, KeyedProcessFunction<String, Pedido, Tuple2<String, Double>>.Context ctx,
            Collector<Tuple2<String, Double>> out) throws Exception {
        Logger.getLogger(this.getClass().getSimpleName()).fine(pedido.toString());
        Double valorAcumulado = state.value();
        if (valorAcumulado == null){
            valorAcumulado = Double.valueOf(0);
        }
        Double valorAtualizado = Double.sum(valorAcumulado, pedido.getPrecoTotal());
        state.update(valorAtualizado);

        ctx.timerService().registerEventTimeTimer(System.currentTimeMillis() + 1000);;
    }

    @Override
    public void onTimer(long timestamp, KeyedProcessFunction<String, Pedido, Tuple2<String, Double>>.OnTimerContext ctx,
            Collector<Tuple2<String, Double>> out) throws Exception {
            Double valorAcumulado = state.value();
            Tuple2<String, Double> tuple2 = Tuple2.of(ctx.getCurrentKey(), valorAcumulado);
            out.collect(tuple2);
    }

}
