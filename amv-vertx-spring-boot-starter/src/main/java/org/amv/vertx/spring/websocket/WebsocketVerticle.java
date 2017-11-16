package org.amv.vertx.spring.websocket;

import com.google.common.collect.ImmutableList;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Slf4j
public class WebsocketVerticle extends AbstractVerticle {

    private final WebsocketProperties websocketProperties;
    private final List<WebsocketHandler> websocketHandlers;

    public WebsocketVerticle(WebsocketProperties websocketProperties, List<WebsocketHandler> websocketHandlers) {
        this.websocketProperties = requireNonNull(websocketProperties);
        this.websocketHandlers = ImmutableList.copyOf(requireNonNull(websocketHandlers));
    }

    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer()
                .websocketHandler(this::handle)
                .listen(websocketProperties.getPort());

        log.info("Websocket server started on port {}", httpServer.actualPort());
    }

    private void handle(ServerWebSocket ws) {
        Optional<WebsocketHandler> handler = findEligibleHandler(ws);

        if (!handler.isPresent()) {
            ws.reject(404);
            return;
        }

        ws.accept();

        handler.get().handle(ws);
    }

    private Optional<WebsocketHandler> findEligibleHandler(ServerWebSocket ws) {
        return websocketHandlers.stream()
                .filter(handler -> handler.supports(ws))
                .findFirst();
    }
}
