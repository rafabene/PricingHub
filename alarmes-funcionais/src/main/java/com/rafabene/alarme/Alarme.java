package com.rafabene.alarme;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

public class Alarme implements Serializable {

    private String cliente;
    private Double valor;
    private Integer limite;
    private Integer janelaTempoSegundos;
    private LocalDateTime timestamp;
    private static String MENSAGEM = "%1$tY%1$tm%1$td%1$tH%1$tM%1$tS - "
            + "Cliente [%2$s] movimentou acima do limite estipulado [R$ %3$d,00] nos ultimos %4$d segundos. "
            + "Valor movimentado: R$ %5$.2f";

    public static void main(String[] args) {
        System.out.println(
                String.format(new Locale("pt", "BR"), MENSAGEM, LocalDateTime.now(), "Cliente X", 50, 10, 123.00));
    }

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
        return String.format(MENSAGEM, timestamp, cliente, limite, janelaTempoSegundos, valor);
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
