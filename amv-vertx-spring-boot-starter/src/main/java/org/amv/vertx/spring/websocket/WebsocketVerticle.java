package org.amv.vertx.spring.websocket;

import com.google.common.collect.ImmutableList;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

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
        vertx.createHttpServer()
                .websocketHandler(webSocketHandler())
                .listen(websocketProperties.getPort());
    }

    private Handler<ServerWebSocket> webSocketHandler() {
        return ws -> {
            final List<WebsocketHandler> handlers = websocketHandlers.stream()
                    .filter(handler -> handler.supports(ws))
                    .collect(Collectors.toList());

            if (handlers.isEmpty()) {
                ws.reject(404);
            }

            handlers.forEach(handler -> handler.handle(ws));
        };
    }
}
