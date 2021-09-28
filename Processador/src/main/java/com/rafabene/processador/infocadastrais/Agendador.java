package com.rafabene.processador.infocadastrais;

import static org.quartz.CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek;
import static org.quartz.DateBuilder.FRIDAY;
import static org.quartz.DateBuilder.MONDAY;
import static org.quartz.DateBuilder.THURSDAY;
import static org.quartz.DateBuilder.TUESDAY;
import static org.quartz.DateBuilder.WEDNESDAY;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.StdSchedulerFactory;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class Agendador {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "infocadastrais.retry.segundos")
    private int retrySegundos;

    public void agendarDownloadAtivos(@Observes @RuntimeStart Object o) throws SchedulerException{
        logger.info("Processador inicializado");
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        // Define job instance
        JobDetail downloadAtivos =  newJob(DownloadAtivos.class)
            .withIdentity("download", "infoCadastrais")
            .storeDurably(true)
            .usingJobData(DownloadAtivos.RETRY_KEY, retrySegundos)
            .build();

        //Define o Trigger para uma vez por dia 
        Trigger downloadPorDia  = newTrigger()
            .forJob(downloadAtivos)
            .withIdentity("umaVezPorDia")
            .startNow()
            .withSchedule(atHourAndMinuteOnGivenDaysOfWeek(9, 0, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY))
            //.withSchedule(CronScheduleBuilder.cronSchedule("*/10 * * * * ?"))
            .build();
        
        //Define o Trigger para executar agora
        Trigger executaAgora  = newTrigger()
            .forJob(downloadAtivos)
            .withIdentity("executaAgora")
            // Espera o servidor inicializar
            .startAt(DateBuilder.futureDate(5, IntervalUnit.SECOND))
            .build();

        scheduler.addJob(downloadAtivos, false);
        scheduler.scheduleJob(downloadPorDia);
        scheduler.scheduleJob(executaAgora);
        logger.info("Próxima execução diária em: " + downloadPorDia.getNextFireTime());
    }
    
}
