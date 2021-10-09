package com.rafabene.processador.infocadastrais;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

import com.rafabene.processador.dominio.entidade.Ativo;
import com.rafabene.processador.dominio.entidade.TipoPrecificacao;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.glassfish.jersey.message.GZipEncoder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.TriggerKey;

@DisallowConcurrentExecution
public class DownloadAtivos implements Job{

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public static final String RETRY_SECONDS = "infocadastrais.retry.segundos";
    public static final String DOWNLOAD_URL = "infocadastrais.download.url";
    public static final String TOKEN = "infocadastrais.download.token";


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        NamedCache<String, Boolean> jobsControl = CacheFactory.getCache("jobs");
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        String jobHoje = sdf.format(new Date());
        Boolean executouHoje = jobsControl.get(jobHoje);
        if (executouHoje != null && executouHoje){
            logger.warning("Download do cadastro de ativos já executado/em execução para o dia de hoje");
            return;
        }
        logger.info("Executando download do cadastro de ativos para o dia " + jobHoje);
        try{
            downloadAtivos(jobExecutionContext);
            Date proximaExecucao = jobExecutionContext
                .getScheduler()
                    .getTrigger(TriggerKey.triggerKey("umaVezPorDia"))
                        .getNextFireTime();
            logger.info("Próximo download em " + proximaExecucao);
            jobsControl.put(jobHoje, true);
        }catch(Exception e){
            e.printStackTrace();
            retryJob(jobExecutionContext);
        }
}

    private void retryJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        int retrySegundos = jobExecutionContext.getMergedJobDataMap().getInt(RETRY_SECONDS);
        logger.info(String.format("Download falhou. Tentando em %s segundos", retrySegundos));

        try {
            Thread.sleep(retrySegundos * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        throw new JobExecutionException( "Não consegui fazer o download", true);
    }

    private void downloadAtivos(JobExecutionContext jobExecutionContext) throws IOException {
        logger.info("Tentando fazer o download. " + (jobExecutionContext.getRefireCount() + 1) + "º tentativa(s)");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        AtivosRestResource ativosResource = RestClientBuilder.newBuilder()
            .baseUri(URI.create(dataMap.getString(DOWNLOAD_URL)))
            .register(GZipEncoder.class)
            .build(AtivosRestResource.class);
        InputStream is = ativosResource.getAtivos("Bearer " + dataMap.getString(TOKEN));
        BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
        NamedCache<Long, Ativo> ativosEmMemoria = CacheFactory.getCache("ativos");
        String linha = null;
        while ((linha = br.readLine()) != null && !linha.trim().equals("")){
            try(Scanner scanner = new Scanner(linha).useDelimiter(",")){
                Ativo ativo = new Ativo(scanner.nextLong(), scanner.next().trim(), TipoPrecificacao.valueOf(scanner.next().trim()));
                ativosEmMemoria.put(ativo.getId(), ativo);
            }
        }
        logger.info("Download de Ativos concluido");
    }
    
}
