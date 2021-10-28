package com.rafabene.alarme.flink.functions;

import java.util.logging.Logger;

import com.rafabene.pedido.dominio.vo.Pedido;

import org.apache.flink.api.common.functions.AggregateFunction;

public class AggregatePedidos implements AggregateFunction<Pedido, Double, Double> {

    @Override
    public Double createAccumulator() {
        return Double.valueOf(0);
    }

    @Override
    public Double add(Pedido pedido, Double accumulator) {
        Logger.getLogger(this.toString()).fine("Pedido para calculo: " + pedido.toString());
        Double valor = accumulator + pedido.getPrecoTotal();
        Logger.getLogger(this.toString()).fine("Valor acumulado: " + valor);
        return valor;
    }

    @Override
    public Double getResult(Double accumulator) {
        Logger.getLogger(this.toString()).fine("Resultado do acumulo de pedidos: " + accumulator);
        return accumulator;
    }

    @Override
    public Double merge(Double a, Double b) {
        return a + b;
    }

}
