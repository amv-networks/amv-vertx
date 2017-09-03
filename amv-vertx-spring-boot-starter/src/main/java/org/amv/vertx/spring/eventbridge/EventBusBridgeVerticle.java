package org.amv.vertx.spring.eventbridge;

import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.sockjs.SockJSHandler;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.requireNonNull;

@Slf4j
public class EventBusBridgeVerticle extends AbstractVerticle {

    private final SockJSHandler sockJSHandler;

    public EventBusBridgeVerticle(SockJSHandler sockJSHandler) {
        this.sockJSHandler = requireNonNull(sockJSHandler);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        String name = this.getClass().getSimpleName();
        int port = 8080;

        log.info("Starting {} on port {}...", name, port);

        Router router = Router.router(vertx);
        router.route("/eventbus/*").handler(sockJSHandler);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(port)
                .subscribe(server -> {
                    log.info("Started {} on port {}", name, server.actualPort());
                    startFuture.complete();
                }, startFuture::fail);
    }

}
