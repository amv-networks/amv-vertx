package org.amv.vertx.spring.websocket;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.SocketUtils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.isNull;

@ConfigurationProperties("vertx.websocket")
public class WebsocketProperties {
    private static final AtomicReference<Integer> internalPort = new AtomicReference<>();

    private int port;

    public int getPort() {
        synchronized (internalPort) {
            if (port == 0 && isNull(internalPort.get())) {
                internalPort.set(SocketUtils.findAvailableTcpPort(10_000));
            }
        }
        return port != 0 ? port : internalPort.get();
    }
}
