package com.rafabene.recebepedidos.kafka;

import java.util.concurrent.ExecutionException;

import javax.enterprise.inject.Any;

import com.rafabene.recebepedidos.rest.OrdemCompra;

@Any
public interface KafkaService {

    public void enviarOrdemCompra(OrdemCompra ordemCompra) throws InterruptedException, ExecutionException;

}
