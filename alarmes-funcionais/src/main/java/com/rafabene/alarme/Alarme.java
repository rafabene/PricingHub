package com.rafabene.alarme;

import java.time.LocalDateTime;
import java.util.Objects;

public class Alarme {

    private String cliente;
    private Double valor;
    private Integer limite;
    private Integer janelaTempoSegundos;
    private LocalDateTime timestamp;

    public Alarme(String cliente, Double valor, Integer limite, Integer janelaTempoSegundos) {
        this.cliente = cliente;
        this.valor = valor;
        this.limite = limite;
        this.janelaTempoSegundos = janelaTempoSegundos;
        this.timestamp = LocalDateTime.now();
    }

    public String getCliente() {
        return cliente;
    }

    public String getMensagem() {
        return String.format(
            "%1$tY%1$tm%1$td%1$tH%1$tM%1$tS - Cliente [%2$s] movimentou acima do limite estipulado [%3$d] nos ultimos %4$d segundos. Valor movimentado: %5$.2f",
            timestamp, cliente, limite, janelaTempoSegundos, valor);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Alarme)) {
            return false;
        }
        Alarme alarme = (Alarme) o;
        return Objects.equals(cliente, alarme.cliente) && Objects.equals(valor, alarme.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, valor);
    }

}
