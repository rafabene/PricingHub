package com.rafabene.recebepedidos.socket;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.rafabene.recebepedidos.dominio.vo.OrdemCompra;
import com.rafabene.recebepedidos.kafka.KafkaService;

public class ProtocoloOrdemCompra {

    private OrdemCompra ordemCompra;

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    public String processaEntrada(String entrada)  {
        String resposta = null;
        switch (entrada) {
        case "BEGIN":
            ordemCompra = new OrdemCompra();
            resposta = "Pronto.";
            break;
        case "END":
            resposta = enviaOrdemCompra();
            break;
        case "GOODBYE":
            resposta = "Good bye.";
            break;
        default:
            resposta = "Comando não conhecido";
        }
        if (entrada.startsWith("QUANTIDADE")) {
            int quantidade = Integer.valueOf(entrada.substring("QUANTIDADE".length()).trim());
            ordemCompra.setQuantidade(quantidade);
            resposta = ordemCompra.toString();
        } else if (entrada.startsWith("TOKEN")) {
            String token = entrada.substring("TOKEN".length()).trim();
            ordemCompra.setTokenCliente(token);
            resposta = ordemCompra.toString();
        } else if (entrada.startsWith("ATIVO")) {
            String ativo = entrada.substring("ATIVO".length()).trim();
            ordemCompra.setNomeAtivo(ativo);
            resposta = ordemCompra.toString();
        }
        return resposta;
    }

    private String enviaOrdemCompra()  {
        if (ordemCompra == null){
            return "Comando BEGIN não enviado. Não existe ordem para envio";
        }
        Set<ConstraintViolation<OrdemCompra>> violations = validator.validate(ordemCompra);
        if (!violations.isEmpty()){
            StringBuilder sb = new StringBuilder();
            sb.append("Ordem: " + ordemCompra.toString()  + "\n");
            sb.append("Erros de validação da Ordem:  \n");
            for (ConstraintViolation<OrdemCompra> constraintViolation : violations) {
                sb.append(constraintViolation.getPropertyPath() + " - " + constraintViolation.getMessage() + "\n");
            }
            return sb.toString();
        }else{
            KafkaService kafkaService = getKafkaServiceFromCDI();
            try {
                kafkaService.enviarOrdemCompra(ordemCompra);
            } catch (InterruptedException | ExecutionException e) {
                return "Erro ao enviar Ordem: " + e.getMessage();
            }
            return String.format("Ordem %s enviada para processamento", ordemCompra);
        }
    }

    private KafkaService getKafkaServiceFromCDI() {
        BeanManager bm = CDI.current().getBeanManager();
        Set<Bean<?>> setBeans = bm.getBeans(KafkaService.class, new Annotation[]{});
        Bean<KafkaService> bean = (Bean<KafkaService>) bm.resolve(setBeans);
        CreationalContext<KafkaService> creationalContext = bm.createCreationalContext(bean);
        KafkaService kafkaService = (KafkaService) bean.create(creationalContext);
        return kafkaService;
    }

}
