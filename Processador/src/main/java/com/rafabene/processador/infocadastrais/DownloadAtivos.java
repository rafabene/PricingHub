package com.rafabene.processador.infocadastrais;

import java.util.Date;
import java.util.logging.Logger;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.TriggerKey;

@DisallowConcurrentExecution
public class DownloadAtivos implements Job{

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public static final String RETRY_KEY = "infocadastrais.retry.segundos";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            downloadAtivos(jobExecutionContext);
            Date proximaExecucao = jobExecutionContext
                .getScheduler()
                    .getTrigger(TriggerKey.triggerKey("umaVezPorDia"))
                        .getNextFireTime();
            logger.info("Próximo download em " + proximaExecucao);
        }catch(Exception e){
            e.printStackTrace();
            retryJob(jobExecutionContext);
        }
}

    private void retryJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        int retrySegundos = jobExecutionContext.getMergedJobDataMap().getInt(RETRY_KEY);
        logger.info(String.format("Download falhou. Tentando em %s segundos", retrySegundos));

        try {
            Thread.sleep(retrySegundos * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JobExecutionException executionException =  new JobExecutionException("Não consegui fazer o download");
        executionException.setRefireImmediately(true);
        throw executionException;
    }

    private void downloadAtivos(JobExecutionContext jobExecutionContext) throws DownloadException {
        logger.info("Tentando fazer o download. " + (jobExecutionContext.getRefireCount() + 1) + "º tentativa(s)");
         throw new DownloadException();
        //logger.info("Download concluido");
    }
    
}
