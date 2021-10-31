
package com.rafabene.precificacao;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.server.Server;

class MainTest {

    private static Server server;
    private static String serverUrl;

    @BeforeAll
    public static void startTheServer() throws Exception {
        System.setProperty("carregaPrecoPadrao", "false");
        server = Server.create().start();
        serverUrl = "http://localhost:" + server.port();
    }

    @Test
    void testObterPrecoVazio() {
        Client client = ClientBuilder.newClient();

        Response resposta = client
                .target(serverUrl)
                .path("preco/MA")                
                .request()
                .header("token", "qualquer")
                .get(Response.class);
        Assertions.assertEquals(204, resposta.getStatus());
    }

    @Test
    void testObterPrecoCodigoErrado() {
        Client client = ClientBuilder.newClient();

        Response resposta = client
                .target(serverUrl)
                .path("preco/MX")
                .request()
                .header("token", "qualquer")
                .get(Response.class);
        Assertions.assertEquals(406, resposta.getStatus());
    }

    @Test
    void testObterPrecoSemToken() {
        Client client = ClientBuilder.newClient();

        Response resposta = client
                .target(serverUrl)
                .path("preco/B")
                .request()
                .get(Response.class);
        Assertions.assertEquals(406, resposta.getStatus());
    }

    @Test
    void testSetarPrecoCodigoErrado() {
        Client client = ClientBuilder.newClient();

        Response resposta = client
                .target(serverUrl)
                .path("preco/MX")
                .request()
                .put(Entity.text("1.0"));
        Assertions.assertEquals(406, resposta.getStatus());
    }

    @Test
    void testSetarPreco() {
        Client client = ClientBuilder.newClient();

        Response resposta = client
                .target(serverUrl)
                .path("preco/MB")
                .request()
                .put(Entity.text("1.0"));
        Assertions.assertEquals(204, resposta.getStatus());
    }

    @Test
    void testSetarPrecoNaoNumerico() {
        Client client = ClientBuilder.newClient();

        Response resposta = client
                .target(serverUrl)
                .path("preco/MB")
                .request()
                .put(Entity.text("X"));
        Assertions.assertEquals(500, resposta.getStatus());
    }

    @AfterAll
    static void destroyClass() {
        CDI<Object> current = CDI.current();
        ((SeContainer) current).close();
    }
}
