package com.rafabene.processador.infocadastrais;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

@ApplicationScoped
public class ControleAtivosDia {

    private NamedCache<String, Boolean> jobsControl = CacheFactory.getCache("jobs");
    private  SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");


    public boolean temPrecoDia(){
        String jobHoje = sdf.format(new Date());
        Boolean executouHoje = jobsControl.get(jobHoje);
        return executouHoje != null && executouHoje;
    }

    public void marcaPrecoDiaObtido(){
        String jobHoje = sdf.format(new Date());
        jobsControl.put(jobHoje, true);
    }
    
}
