package com.rafabene.processador.processapedidos;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.rafabene.processador.dominio.entidade.Ativo;
import com.rafabene.processador.dominio.repositorio.RepositorioAtivos;
import com.rafabene.processador.dominio.vo.OrdemCompra;
import com.rafabene.processador.infocadastrais.ControleAtivosDia;
import com.rafabene.processador.precificador.PrecificadorResource;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class RecebePedidos {

    @Inject
    private ControleAtivosDia controleAtivosDia;

    @Inject
    private RepositorioAtivos repositorioAtivos;

    @Inject
    private KafkaConsumer<String, OrdemCompra> consumerOrdemCompra;

    @Inject
    private KafkaProducer<String, OrdemCompra> producerOrdemCompra;

    @Inject
    @ConfigProperty(name = "ordemcompra.dlq")
    private String ordemCompraDLQ;

    @Inject
    @ConfigProperty(name = "precificador.url")
    private String precificadorURL;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void inicializarRecebimentoPedidos(@Observes @Priority(2) @RuntimeStart Object o)
            throws InterruptedException {

        // Aguardando download de Ativos
        while (true) {
            if (!controleAtivosDia.temPrecoDia()) {
                logger.info("Aguardando 10s o download dos ativos do dia.");
                Thread.sleep(10 * 1000);
            } else {
                break;
            }
        }
        logger.info("Iniciando o processamento dos pedidos");
        obterPedidosKafka();
    }

    private void obterPedidosKafka() {
        while (true) {
            ConsumerRecords<String, OrdemCompra> records = consumerOrdemCompra.poll(Duration.ofSeconds(1));
            records.forEach(f -> processaOrdem(f.value()));
        }
    }

    private void processaOrdem(OrdemCompra ordemCompra) {
        Ativo ativo = repositorioAtivos.obtemAtivoPorNome(ordemCompra.getNomeAtivo());
        if (ativo == null) {
            ordemCompraComProblema(ordemCompra, "Não existe ativo cadastrado com o nome " + ordemCompra.getNomeAtivo());
        } else {
            PrecificadorResource precificadorResource = RestClientBuilder.newBuilder()
                    .baseUri(URI.create(precificadorURL)).build(PrecificadorResource.class);
            Double preco = precificadorResource.getPreco(ativo.getTipoPrecificacao());
            if (preco == null) {
                ordemCompraComProblema(ordemCompra,
                        String.format("Não foi possível obter preço para a ordem %s. Tipo de precificação do ativo: %s",
                                ordemCompra, ativo.getTipoPrecificacao()));
            } else {
                logger.info(preco.toString());
            }
        }
    }

    private void ordemCompraComProblema(OrdemCompra ordemCompra, String causa) {
        logger.info(String.format("Ordem de compra %s descaradatada. Ordem será enviada para DLQ: %s - Motivo: %s",
                ordemCompra, ordemCompraDLQ, causa));
        ProducerRecord<String, OrdemCompra> producerRecord = new ProducerRecord<String, OrdemCompra>(ordemCompraDLQ,
                ordemCompra);
        producerRecord.headers().add("causa", causa.getBytes(Charset.forName("UTF-8")));
        try {
            producerOrdemCompra.send(producerRecord).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}