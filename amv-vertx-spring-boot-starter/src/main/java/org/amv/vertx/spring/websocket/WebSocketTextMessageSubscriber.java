package org.amv.vertx.spring.websocket;

import io.vertx.rxjava.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

@Slf4j
public class WebSocketTextMessageSubscriber extends BaseSubscriber<Object> {

    private final AtomicReference<ServerWebSocket> websocket;

    public WebSocketTextMessageSubscriber(ServerWebSocket websocket) {
        requireNonNull(websocket);
        this.websocket = new AtomicReference<>(websocket);

        websocket.endHandler(event -> {
            this.websocket.set(null);
            this.dispose();
        });
    }

    @Override
    protected void hookOnNext(Object value) {
        websocket().subscribe(ws -> {
            ws.writeTextMessage(value.toString());
        });
    }

    @Override
    protected void hookOnError(Throwable t) {
        log.error("", t);
    }

    @Override
    protected void hookFinally(SignalType type) {
        websocket().subscribe(ws -> {
            try {
                ws.end();
            } catch (IllegalStateException e) {
                log.warn("", e);
            }
        });
    }

    private Mono<ServerWebSocket> websocket() {
        return Optional.ofNullable(this.websocket.get())
                .map(Mono::just)
                .orElseGet(() -> {
                    if (!this.isDisposed()) {
                        this.dispose();
                    }
                    return Mono.empty();
                });
    }
}
