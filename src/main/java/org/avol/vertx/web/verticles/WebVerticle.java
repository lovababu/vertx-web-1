package org.avol.vertx.web.verticles;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import org.avol.vertx.web.model.Message;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Lovababu on 1/22/2017.
 */
public class WebVerticle extends AbstractVerticle {

    private HttpServer server;
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void start() throws Exception {
        server = vertx.createHttpServer();

        server.requestHandler(request -> {

            // This handler gets called for each request that arrives on the server
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "application/json");

            // Write to the response and end it
            response.end(generateMessage());
        });

        server.listen(80);
    }

    @Override
    public void stop() throws Exception {
        server.close();
    }

    private String generateMessage() {
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = new Message();
        message.setId(atomicInteger.getAndIncrement());
        try {
            message.setMessage("Hello, welcome to Ansible AWS session . " + InetAddress.getLocalHost().getHostName());
            message.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            return "Hello, Welcome to Vert.x.";
        }

    }
}
