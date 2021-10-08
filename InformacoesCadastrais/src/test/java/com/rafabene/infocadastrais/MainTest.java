
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
    void testGetAtivos() {
        Client client = ClientBuilder.newClient();

        Response response = client
                .target(serverUrl)
                .path("ativos")
                .request()
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwcm9jZXNzYWRvciIsImlhdCI6MTYzMzAzNTI4NiwiZXhwIjoxNjY0NTkyNzc4LCJhdWQiOiJpbmZvY2FkYXN0cmFpcyIsInN1YiI6InJhZmFiZW5lQGdtYWlsLmNvbSIsInVwbiI6IlByb2Nlc3NhZG9yIiwiZ3JvdXBzIjpbInNlcnZpY2UiLCJkb3dubG9hZCJdfQ.f42fA_rJ0RYVsctQAn6lt8zDSA8vfc9IGe4hc4Uhm0CC0sp8cBYG-_SmVM8pxO5n1JrxxkKZxPo_rusOTCxsAfPeZg-EycK6f8F-rwqtJph-SD08S7TBrfUSs2jHE_NxUPMH4vggvNBuc9qmCSsZu2adGAL2VpQhFCfcSHWSW_0mbCjqBAKrjUg6qtMedwR6xxZhFz0xw1UgKJByREUvrFYEN2RyMpY6y6K42kOn6tq0f_sbB8JUAeAHf75SfhEv74UfVLXGcXi19544a5gwxXhiayt9a_j1RRVjhD-6CEmORjcta8K-rQkaQNdsLBbFwHbNtcsSx4krkGZiu7cyAA")
                .get(Response.class);
        Assertions.assertEquals(200, response.getStatus(), "GET status code");
        Assertions.assertTrue(response.getHeaderString("Content-Encoding").equals("gzip"));
        Assertions.assertTrue(response.getHeaderString("transfer-encoding").equals("chunked"));
    }

    @Test
    void testGetAtivosWithoutAuthentication() {
        Client client = ClientBuilder.newClient();

        Response response = client
                .target(serverUrl)
                .path("ativos")
                .request()
                .get(Response.class);
        Assertions.assertEquals(403, response.getStatus(), "GET status code");
    }

    @AfterAll
    static void destroyClass() {
        CDI<Object> current = CDI.current();
        ((SeContainer) current).close();
    }
}
