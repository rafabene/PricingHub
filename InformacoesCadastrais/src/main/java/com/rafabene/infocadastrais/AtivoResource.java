
package com.rafabene.infocadastrais;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.rafabene.infocadastrais.infra.Compress;

import org.glassfish.jersey.server.ChunkedOutput;


@Path("/ativos")
@RequestScoped
public class AtivoResource {

    @Inject
    private AtivoProvider ativoProvider;

    @GET
    @Produces("text/plain")
    @Compress
    public ChunkedOutput<String> getArquivoAtivos() {
        final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);
        new Thread(){
            public void run() {
                String line = null;
                try (BufferedReader br = Files.newBufferedReader(Paths.get(ativoProvider.getArquivo().toURI()), Charset.forName("US-ASCII"))){
                    while ((line = br.readLine()) != null){
                        output.write(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
        return output;
    }


}
