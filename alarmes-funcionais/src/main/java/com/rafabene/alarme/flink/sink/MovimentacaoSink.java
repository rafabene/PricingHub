package com.rafabene.alarme.flink.sink;

import java.util.logging.Logger;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class MovimentacaoSink implements SinkFunction<Tuple2<String, Double>> {

    @Override
    public void invoke(Tuple2<String, Double> tupla, Context context) throws Exception {
        String cliente = tupla.f0;
        Double valorAcumulado = tupla.f1;
        Logger.getLogger(this.getClass().toString())
                .fine(String.format("Valor acumulado para o cliente %s é %.2f", cliente, valorAcumulado));
        NamedCache<String, Double> valorAcumuladoPorCliente = CacheFactory.getCache("valorAcumulado");
        valorAcumuladoPorCliente.put(cliente, valorAcumulado);
    }

}
