package org.amv.vertx.spring.websocket;

import io.vertx.core.Handler;
import io.vertx.rxjava.core.http.ServerWebSocket;

public interface WebsocketHandler extends Handler<ServerWebSocket> {
    boolean supports(ServerWebSocket event);
}
