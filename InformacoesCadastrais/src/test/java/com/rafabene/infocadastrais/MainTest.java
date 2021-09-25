
package com.rafabene.infocadastrais;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
        server = Server.create().start();
        serverUrl = "http://localhost:" + server.port();
    }

    @Test
    void testHelloWorld() {
        Client client = ClientBuilder.newClient();

        Response response = client
                .target(serverUrl)
                .path("ativos")
                .request()
                .get(Response.class);
        Assertions.assertEquals(200, response.getStatus(), "GET status code");
        Assertions.assertTrue(response.getHeaderString("Content-Encoding").equals("gzip"));
        Assertions.assertTrue(response.getHeaderString("transfer-encoding").equals("chunked"));
    }

    @AfterAll
    static void destroyClass() {
        CDI<Object> current = CDI.current();
        ((SeContainer) current).close();
    }
}
