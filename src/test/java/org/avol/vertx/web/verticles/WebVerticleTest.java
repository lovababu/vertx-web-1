package org.avol.vertx.web.verticles;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by dpadal on 1/23/2017.
 */
@RunWith(VertxUnitRunner.class)
public class WebVerticleTest {

    private Vertx vertx;
    private int port = 80;

    @Before
    public void setUp(TestContext context) throws IOException {
        vertx = Vertx.vertx();
        /*ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();*/
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject().put("http.port", port));
        vertx.deployVerticle(WebVerticle.class.getName(), options,
                context.asyncAssertSuccess());
    }

    @Test
    public void testMyApplication(TestContext context) {
        final Async async = context.async();
        vertx.createHttpClient().getNow(port, "localhost", "/", response -> response.handler(body -> {
            context.assertTrue(body.toJsonObject().containsKey("message"));
            context.assertTrue(body.toJsonObject().containsKey("date"));
            context.assertTrue(body.toJsonObject().containsKey("id"));
            async.complete();
        }));
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }
}
