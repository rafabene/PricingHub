package com.rafabene.alarme;

import com.rafabene.pedido.dominio.vo.Pedido;

import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class ProcessaPedidos extends KeyedProcessFunction<String, Pedido, Alarme> {

    @Override
    public void processElement(Pedido pedido, KeyedProcessFunction<String, Pedido, Alarme>.Context context,
            Collector<Alarme> collector) throws Exception {
        
            Alarme alarme = new Alarme(pedido.getTokenCliente());
            collector.collect(alarme);
    }

}
