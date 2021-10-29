package com.rafabene.recebepedidos.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class OrdemCompraSocket extends Thread {

    private static final String FORMATO = "Nome Ativo, 0 (quantidade), token do cliente";

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Socket socket;

    public OrdemCompraSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os), true);

            String inputLine, outputLine;
            pw.println("Bem vindo. \nInforme as ordens de compra no formato: " + FORMATO);
            pw.println("Digite BYE para finalizar a sessão\n");
            while ((inputLine = br.readLine()) != null) {
                outputLine = processaLinha(inputLine);
                pw.println(outputLine);
                if (inputLine.equals("BYE")) {
                    break;
                }
            }
            socket.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private String processaLinha(String inputLine) {
        try {
            String[] campos = inputLine.split(",");
            String nomeAtivo = campos[0].trim();
            int quantidade = Integer.valueOf(campos[1].trim());
            String token = campos[2].trim();
            OrdemCompra ordemCompra = new OrdemCompra(nomeAtivo, quantidade, token);
            return enviaOrdemCompra(ordemCompra);
        } catch (NumberFormatException e){
            return "ERRO: A quantidade deve ser um número inteiro. " + e.getMessage();
        } catch (ArrayIndexOutOfBoundsException e){
            return "ERRO: A linha deve serguir o formato: " + FORMATO;
        } catch (Exception e) {
            logger.log(Level.SEVERE,e.getMessage(), e);
            return "ERRO: " + e.getMessage();
        }
    }

    private String enviaOrdemCompra(OrdemCompra ordemCompra) {
        Set<ConstraintViolation<OrdemCompra>> violations = validator.validate(ordemCompra);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Ordem: " + ordemCompra.toString() + "\n");
            sb.append("Erros de validação da Ordem:  \n");
            for (ConstraintViolation<OrdemCompra> constraintViolation : violations) {
                sb.append(constraintViolation.getPropertyPath() + " - " + constraintViolation.getMessage() + "\n");
            }
            return sb.toString();
        } else {
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
        Set<Bean<?>> setBeans = bm.getBeans(KafkaService.class, new Annotation[] {});
        Bean<KafkaService> bean = (Bean<KafkaService>) bm.resolve(setBeans);
        CreationalContext<KafkaService> creationalContext = bm.createCreationalContext(bean);
        KafkaService kafkaService = (KafkaService) bean.create(creationalContext);
        return kafkaService;
    }

}
