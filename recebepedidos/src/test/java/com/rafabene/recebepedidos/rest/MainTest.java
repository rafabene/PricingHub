
package com.rafabene.recebepedidos.rest;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.ext.cdi1x.internal.CdiComponentProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.faulttolerance.FaultToleranceExtension;
import io.helidon.microprofile.metrics.MetricsCdiExtension;
import io.helidon.microprofile.server.JaxRsCdiExtension;
import io.helidon.microprofile.server.ServerCdiExtension;
import io.helidon.microprofile.tests.junit5.AddBean;
import io.helidon.microprofile.tests.junit5.AddExtension;
import io.helidon.microprofile.tests.junit5.DisableDiscovery;
import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
@DisableDiscovery
@AddExtension(ServerCdiExtension.class)
@AddExtension(JaxRsCdiExtension.class)
@AddExtension(CdiComponentProvider.class)
@AddExtension(FaultToleranceExtension.class)
@AddExtension(MetricsCdiExtension.class)
@AddBean(RecebePedidosResource.class)
@AddBean(ProcessionExceptionMapper.class)
@AddBean(KafkaDispatcherMock.class)
public class MainTest {
    
    @Inject
    private WebTarget webTarget;

    @Test
    void testEmptyBody() {
        Response response = webTarget.path("/pedidos").request().post(null);
        Assertions.assertEquals(400, response.getStatus(), "Bad request");

    }

    @Test
    void testWrongContentType() {
        Response response = webTarget.path("/pedidos").request().post(Entity.text(""));
        Assertions.assertEquals(415, response.getStatus(), "Unsupported Media Type");

    }

    @Test
    void testWrongJson() {
        Response response = webTarget.path("/pedidos").request()
                .post(Entity.json("{\"versa1\",\"ordemCompra\": {\"nomeAtivo\": \"Teste\",\"quantidade\": 1,\"tokenCliente\": \"dasdad\"}}"));
        Assertions.assertEquals(406, response.getStatus(), "Not acceptable");
    }

    @Test
    void testJsonMissingFields() {
        Response response = webTarget.path("/pedidos").request()
                .post(Entity.json("{\"versao\": \"V1\",\"ordeCompra\": {\"nomeAtivo\": \"Teste\",\"quantidade\": 1,\"tokenCliente\": \"dasdad\"}}"));
        Assertions.assertEquals(400, response.getStatus(), "Bad request");
    }


    @Test
    void testWrongVersion() {
        Response response = webTarget.path("/pedidos").request()
                .post(Entity.json("{\"versao\": \"V2\",\"ordemCompra\": {\"nomeAtivo\": \"Teste\",\"quantidade\": 1,\"tokenCliente\": \"dasdad\"}}"));
        Assertions.assertEquals(406, response.getStatus(), "Not acceptable");
    }


    @Test
    void testOk() {
        Response response = webTarget.path("/pedidos").request()
                .post(Entity.json("{\"versao\": \"V1\",\"ordemCompra\": {\"nomeAtivo\": \"Teste\",\"quantidade\": 1,\"tokenCliente\": \"dasdad\"}}"));
        Assertions.assertEquals(202, response.getStatus(), "Accepted");
    }

    @Test
    void testTimeout() {
        Response response = webTarget.path("/pedidos").request()
                .post(Entity.json("{\"versao\": \"V1\",\"ordemCompra\": {\"nomeAtivo\": \"Timeout\",\"quantidade\": 1,\"tokenCliente\": \"dasdad\"}}"));
        Assertions.assertEquals(500, response.getStatus(), "Accepted");
    }

}
