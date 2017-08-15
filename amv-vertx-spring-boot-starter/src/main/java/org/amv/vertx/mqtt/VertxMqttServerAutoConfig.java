package org.amv.vertx.mqtt;

import io.vertx.mqtt.MqttServerOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.mqtt.MqttServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Objects.requireNonNull;

@Slf4j
@Configuration
@ConditionalOnClass(MqttServer.class)
@EnableConfigurationProperties(VertxMqttProperties.class)
@ConditionalOnProperty(value = "vertx.mqtt.enabled", havingValue = "true")
public class VertxMqttServerAutoConfig {

    private final VertxMqttProperties properties;

    @Autowired
    public VertxMqttServerAutoConfig(VertxMqttProperties properties) {
        this.properties = requireNonNull(properties);
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnBean(Vertx.class)
    @ConditionalOnMissingBean(MqttServer.class)
    public MqttServer rxMqttServer(Vertx vertx) {
        return MqttServer.create(vertx, mqttServerOptions());
    }

    @Bean
    @ConditionalOnMissingBean(MqttServerOptions.class)
    public MqttServerOptions mqttServerOptions() {
        return new MqttServerOptions()
                .setHost(properties.getHost())
                .setPort(properties.getPort());
    }

}
