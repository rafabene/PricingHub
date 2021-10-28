package com.rafabene.recebepedidos.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.net.ServerSocketFactory;

import org.eclipse.microprofile.config.Config;

import io.helidon.microprofile.cdi.RuntimeStart;

@ApplicationScoped
public class InicializaSocket {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void startSocket(@Observes @RuntimeStart Config config) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(5000);
                    logger.info("Escutando por pedidos via Socket na porta 5000");

                    while (true) {
                        new OrdemCompraSocket(serverSocket.accept()).start();
                    }
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }

            }

        }).start();

    }

}
