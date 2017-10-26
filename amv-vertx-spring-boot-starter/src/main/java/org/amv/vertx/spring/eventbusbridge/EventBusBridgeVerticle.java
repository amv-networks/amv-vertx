package org.amv.vertx.spring.eventbusbridge;

import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.sockjs.SockJSHandler;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.requireNonNull;

@Slf4j
public class EventBusBridgeVerticle extends AbstractVerticle {

    private final EventBusBridgeProperties eventBusBridgeProperties;
    private final SockJSHandler sockJSHandler;

    public EventBusBridgeVerticle(EventBusBridgeProperties eventBusBridgeProperties, SockJSHandler sockJSHandler) {
        this.eventBusBridgeProperties = requireNonNull(eventBusBridgeProperties);
        this.sockJSHandler = requireNonNull(sockJSHandler);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        String name = this.getClass().getSimpleName();
        int port = eventBusBridgeProperties.getPort();
        String routeRegex = eventBusBridgeProperties.getRoutePrefix()
                .map(val -> String.format("/%s/*", val))
                .orElseGet(() -> "/*");

        log.info("Starting {} on port {} with route {}...", name, port, routeRegex);

        Router router = Router.router(vertx);
        router.route(routeRegex).handler(sockJSHandler);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(port)
                .subscribe(server -> {
                    log.info("Started {} on port {}", name, server.actualPort());
                    startFuture.complete();
                }, startFuture::fail);
    }

}
