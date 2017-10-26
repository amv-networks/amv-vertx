package org.amv.vertx.spring.eventbusbridge;

import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.handler.sockjs.SockJSHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Objects.requireNonNull;

@Slf4j
@Configuration
@EnableConfigurationProperties(EventBusBridgeProperties.class)
@ConditionalOnProperty("amv.trafficsoft.datahub.eventbus.bridge.enabled")
public class EventBusBridgeAutoConfig {

    private final Vertx vertx;
    private final EventBusBridgeProperties eventBusBridgeProperties;

    @Autowired
    public EventBusBridgeAutoConfig(Vertx vertx, EventBusBridgeProperties eventBusBridgeProperties) {
        this.vertx = requireNonNull(vertx);
        this.eventBusBridgeProperties = requireNonNull(eventBusBridgeProperties);
    }

    @Bean
    public EventBusBridgeVerticle eventBusBridgeVerticle() {
        return new EventBusBridgeVerticle(eventBusBridgeProperties, sockJSHandler());
    }

    @Bean
    public EventBusBridgeEvents eventBusBridgeEvents() {
        String incomingEventPrefix = eventBusBridgeProperties.getIncomingEventPrefix().orElse("");
        String outgoingEventPrefix = eventBusBridgeProperties.getOutgoingEventPrefix().orElse("");

        return new EventBusBridgeEvents(incomingEventPrefix, outgoingEventPrefix);
    }

    @Bean
    public SockJSHandler sockJSHandler() {
        EventBusBridgeEvents eventBusBridgeEvents = eventBusBridgeEvents();

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);

        BridgeOptions options = new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddressRegex(eventBusBridgeEvents.toIncomingEventName(".*")))
                .addOutboundPermitted(new PermittedOptions().setAddressRegex(eventBusBridgeEvents.toOutgoingEventName(".*")));

        sockJSHandler.bridge(options, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                if (log.isDebugEnabled()) {
                    log.debug("Socket connection created {}", event.socket().remoteAddress().host());
                }
            }
            event.complete(true);
        });

        return sockJSHandler;
    }
}
