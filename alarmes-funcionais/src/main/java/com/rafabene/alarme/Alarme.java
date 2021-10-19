package com.rafabene.alarme;

public class Alarme {

    private String cliente;

    public Alarme(String cliente){
        this.cliente = cliente;
    }

    public String getMensagem(){
        return cliente + " movimentou acima do limite estipulado.";
    }
    
}
