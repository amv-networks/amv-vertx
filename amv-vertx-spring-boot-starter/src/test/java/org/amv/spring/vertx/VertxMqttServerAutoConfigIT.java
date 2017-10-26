package org.amv.spring.vertx;

import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.mqtt.MqttServer;
import org.amv.vertx.spring.eventbusbridge.EventBusBridgeProperties;
import org.amv.vertx.spring.eventbusbridge.EventBusBridgeVerticle;
import org.amv.vertx.spring.mqtt.VertxMqttProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {
        VertxMqttServerAutoConfigIT.TestApplictaion.class
})
public class VertxMqttServerAutoConfigIT {
    @SpringBootApplication
    public static class TestApplictaion {

    }

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
    }

    @Test
    public void hasVertxBean() {
        final Vertx bean = applicationContext.getBean(Vertx.class);
        assertThat(bean, is(notNullValue()));
    }

    @Test
    public void hasMqttServerBean() {
        final MqttServer bean = applicationContext.getBean(MqttServer.class);
        assertThat(bean, is(notNullValue()));
    }
    
    @Test
    public void hasMqttPropertiesBean() {
        final VertxMqttProperties bean = applicationContext.getBean(VertxMqttProperties.class);
        assertThat(bean, is(notNullValue()));
        assertThat(bean.isEnabled(), is(true));
    }
}
