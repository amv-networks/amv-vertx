package org.amv.vertx.spring.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j
@Configuration
@EnableConfigurationProperties(WebsocketProperties.class)
@ConditionalOnBean(WebsocketHandler.class)
public class WebsocketAutoConfig {

    private final WebsocketProperties websocketProperties;

    @Autowired
    public WebsocketAutoConfig(WebsocketProperties websocketProperties) {
        this.websocketProperties = requireNonNull(websocketProperties);
    }

    @Bean
    public WebsocketVerticle webSocketVerticle(List<WebsocketHandler> websocketHandlers) {
        return new WebsocketVerticle(websocketProperties, websocketHandlers);
    }
}
