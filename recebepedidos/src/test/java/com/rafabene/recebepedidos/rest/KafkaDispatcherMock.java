package com.rafabene.recebepedidos.rest;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.enterprise.inject.Default;

import com.rafabene.recebepedidos.dominio.vo.OrdemCompra;
import com.rafabene.recebepedidos.kafka.KafkaService;

@Default
public class KafkaDispatcherMock implements KafkaService {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void enviarOrdemCompra(OrdemCompra ordemCompra) throws InterruptedException, ExecutionException {
        logger.info("Mock enviando ordem :" + ordemCompra);
        // Force timeout
        if (ordemCompra.getNomeAtivo().equals("Timeout")) {
            logger.info("FORCING TIMEOUT");
            Thread.sleep(1000);
        }
    }

}
