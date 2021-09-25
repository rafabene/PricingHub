package com.rafabene.infocadastrais;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.eclipse.microprofile.config.Config;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class AtivoProvider {
    
    private File arquivo = null;

    public void setArquivo(@Observes @RuntimeStart Config config){
        this.arquivo = config.getValue("arquivo", File.class);
        if (!arquivo.exists()){
              throw new IllegalArgumentException(String.format("Arquivo %s não existe.", arquivo.getAbsolutePath()));
        }
    }

    public File getArquivo() {
        return arquivo;
    }
}
