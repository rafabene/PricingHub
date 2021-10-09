
package com.rafabene.recebepedidos.rest;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.spi.CDI;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.helidon.microprofile.server.Server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MainTest {

    private static Server server;
    private static String serverUrl;

    @BeforeAll
    public static void startTheServer() throws Exception {
        server = Server.create().start();
        serverUrl = "http://localhost:" + server.port();
    }

    @Test
    void testEmptyBody() {
        Client client = ClientBuilder.newClient();

        Response response = client
                .target(serverUrl)
                .path("pedidos")
                .request()
                .post(null);
        Assertions.assertEquals(400, response.getStatus(), "Bad request");

    }

    @Test
    void testWrongContentType() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target(serverUrl)
                .path("pedidos")
                .request().post(Entity.text(""));
        Assertions.assertEquals(415, response.getStatus(), "Unsupported Media Type");

    }

    @Test
    void testWrongJson() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target(serverUrl)
                .path("pedidos")
                .request().post(Entity.json("{\"nomeAtivo\": \"Teste\",\"quantidade\": 1,\"tokenCliente\": \"dasdad}"));
        Assertions.assertEquals(406, response.getStatus(), "Not acceptable");
    }

    @Test
    void testJsonMissingFields() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target(serverUrl)
                .path("pedidos")
                .request().post(Entity.json("{\"nomeAtivo\": \"Teste\",\"quantidade\": 1"));
        Assertions.assertEquals(406, response.getStatus(), "Not acceptable");
    }

    @Test
    void testOk() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target(serverUrl)
                .path("pedidos")
                .request().post(Entity.json("{\"nomeAtivo\": \"Teste\",\"quantidade\": 1,\"tokenCliente\": \"dasdad\"}"));
        Assertions.assertEquals(202, response.getStatus(), "Accepted");
    }




    @AfterAll
    static void destroyClass() {
        CDI<Object> current = CDI.current();
        ((SeContainer) current).close();
    }
}
