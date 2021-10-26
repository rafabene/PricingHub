package com.rafabene.alarme.flink;

import com.rafabene.alarme.Alarme;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class ProcessaAlarme extends ProcessWindowFunction<Double, Alarme, String, TimeWindow> {

    private Integer limite;
    private Integer janelaTempoSegundos;

    public ProcessaAlarme(Integer limite, Integer janelaTempoSegundos){
        this.limite = limite;
        this.janelaTempoSegundos = janelaTempoSegundos;
    }

    @Override
    public void process(String tokenCliente, ProcessWindowFunction<Double, Alarme, String, TimeWindow>.Context context,
            Iterable<Double> elements, Collector<Alarme> out) throws Exception {
        for (Double valorAcumuladoPedidos : elements) {
            if (valorAcumuladoPedidos > limite) {
                Alarme alarme = new Alarme(tokenCliente, valorAcumuladoPedidos, limite, janelaTempoSegundos);
                out.collect(alarme);
            }
        }

    }

}
