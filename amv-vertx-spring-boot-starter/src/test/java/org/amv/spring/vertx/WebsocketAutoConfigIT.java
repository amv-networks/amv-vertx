package org.amv.spring.vertx;

import io.vertx.core.http.RequestOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.amv.vertx.spring.websocket.WebsocketHandler;
import org.amv.vertx.spring.websocket.WebsocketProperties;
import org.amv.vertx.spring.websocket.WebsocketVerticle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {
        WebsocketAutoConfigIT.TestApplictaion.class
}, properties = {
        "spring.profiles.active=websocket"
})
public class WebsocketAutoConfigIT {
    private static final Buffer TEST_BEACON = Buffer.buffer().appendInt(13);

    @SpringBootApplication
    public static class TestApplictaion {
        @Bean
        public WebsocketHandler websocketHandler() {
            return new WebsocketHandler() {
                @Override
                public boolean supports(ServerWebSocket event) {
                    return true;
                }

                @Override
                public void handle(ServerWebSocket event) {
                    event.accept();

                    event.end(TEST_BEACON);
                }
            };
        }
    }

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
    }

    @Test
    public void hasWebsocketProperties() {
        final WebsocketProperties bean = applicationContext.getBean(WebsocketProperties.class);
        assertThat(bean, is(notNullValue()));
    }

    @Test
    public void hasWebsocketVerticle() {
        final WebsocketVerticle bean = applicationContext.getBean(WebsocketVerticle.class);
        assertThat(bean, is(notNullValue()));
    }

    @Test
    public void connectToWebsocket() throws InterruptedException {
        final Vertx vertx = applicationContext.getBean(Vertx.class);
        final WebsocketProperties properties = applicationContext.getBean(WebsocketProperties.class);

        CountDownLatch latch = new CountDownLatch(1);

        String path = "/" + "test";

        AtomicReference<Buffer> success = new AtomicReference<>();

        vertx.createHttpClient()
                .websocket(new RequestOptions()
                        .setURI(path)
                        .setPort(properties.getPort()), websocket -> {
                    websocket.handler(data -> {
                        success.set(data);
                        latch.countDown();
                    });
                });

        latch.await(1, TimeUnit.SECONDS);

        assertThat(success.get(), is(TEST_BEACON));
    }
}
