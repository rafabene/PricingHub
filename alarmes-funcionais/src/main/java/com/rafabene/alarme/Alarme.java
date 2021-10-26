package com.rafabene.alarme;

public class Alarme {

    private String cliente;
    private Double valor;
    private Integer limite;
    private Integer janelaTempoSegundos;

    public Alarme(String cliente, Double valor, Integer limite, Integer janelaTempoSegundos) {
        this.cliente = cliente;
        this.valor = valor;
        this.limite = limite;
        this.janelaTempoSegundos = janelaTempoSegundos;
    }

    public String getMensagem() {
        return String
            .format("Cliente [%s] movimentou acima do limite estipulado [%d] nos ultimos %d segundos. Valor movimentado: %.2f", 
            cliente, limite, janelaTempoSegundos, valor);
    }

}
