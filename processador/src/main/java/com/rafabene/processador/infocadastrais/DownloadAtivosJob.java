package com.rafabene.processador.infocadastrais;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

import com.rafabene.processador.dominio.entidade.Ativo;
import com.rafabene.processador.dominio.entidade.TipoPrecificacao;
import com.rafabene.processador.dominio.repositorio.RepositorioAtivos;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.glassfish.jersey.message.GZipEncoder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.TriggerKey;

@DisallowConcurrentExecution
public class DownloadAtivosJob implements Job {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private RepositorioAtivos repositorioAtivos;

    public static final String RETRY_SECONDS = "infocadastrais.retry.segundos";
    public static final String DOWNLOAD_URL = "infocadastrais.download.url";
    public static final String TOKEN = "infocadastrais.download.token";
    public static final String REPOSITORIO_ATIVOS = RepositorioAtivos.class.getName();
    public static final String CONTROLE_ATIVOS_DIA = ControleAtivosDia.class.getName();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        ControleAtivosDia controleAtivosDia = (ControleAtivosDia) jobDataMap.get(CONTROLE_ATIVOS_DIA);
        repositorioAtivos = (RepositorioAtivos) jobDataMap.get(REPOSITORIO_ATIVOS);
        if (controleAtivosDia.temPrecoDia()) {
            logger.warning("Download do cadastro de ativos já executado/em execução para o dia de hoje");
            return;
        }
        try {
            logger.info("Tentando fazer o download de ativos para o dia de hoje. " + (jobExecutionContext.getRefireCount() + 1) + "º tentativa(s)");
            String url = jobDataMap.getString(DOWNLOAD_URL);
            String token = jobDataMap.getString(TOKEN);
            downloadAtivos(url, token);
            Date proximaExecucao = jobExecutionContext.getScheduler().getTrigger(TriggerKey.triggerKey("umaVezPorDia"))
                    .getNextFireTime();
            logger.info("Download de Ativos concluido. Próximo download em " + proximaExecucao);
            controleAtivosDia.marcaPrecoDiaObtido();
        } catch (Exception e) {
            e.printStackTrace();
            retryJob(jobDataMap.getInt(RETRY_SECONDS));
        }
    }

    private void retryJob(int retrySegundos) throws JobExecutionException {
        logger.info(String.format("Download falhou. Tentando em %s segundos", retrySegundos));
        try {
            Thread.sleep(retrySegundos * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new JobExecutionException("Não consegui fazer o download", true);
    }

    private void downloadAtivos(String url, String token) throws IOException {
        AtivosRestResource ativosResource = RestClientBuilder.newBuilder().baseUri(URI.create(url))
                .register(GZipEncoder.class).build(AtivosRestResource.class);
        InputStream is = ativosResource.getAtivos("Bearer " + token);
        BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
        String linha = null;
        while ((linha = br.readLine()) != null && !linha.trim().equals("")) {
            try (Scanner scanner = new Scanner(linha).useDelimiter(",")) {
                Ativo ativo = new Ativo(scanner.nextLong(), scanner.next().trim(),
                        TipoPrecificacao.valueOf(scanner.next().trim()));
                repositorioAtivos.adicionar(ativo);
            } catch (Exception e){
                String msg = String.format("Ativo [%s] descartado. Motivo: %s", linha, e.getMessage());
                logger.warning(msg);
            }
        }
    }

}
