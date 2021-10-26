package com.rafabene.alarme.flink;

import com.rafabene.pedido.dominio.vo.Pedido;

import org.apache.flink.api.common.functions.AggregateFunction;

public class AggregatePedidos implements AggregateFunction<Pedido, Double, Double> {

    @Override
    public Double createAccumulator() {
        return Double.valueOf(0);
    }

    @Override
    public Double add(Pedido pedido, Double accumulator) {
        return accumulator + pedido.getPrecoTotal();
    }

    @Override
    public Double getResult(Double accumulator) {
        return accumulator;
    }

    @Override
    public Double merge(Double a, Double b) {
        return a + b;
    }

}
