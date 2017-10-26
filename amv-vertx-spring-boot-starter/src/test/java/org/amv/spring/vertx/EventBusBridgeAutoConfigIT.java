package org.amv.spring.vertx;

import io.vertx.rxjava.core.Vertx;
import org.amv.vertx.spring.eventbusbridge.EventBusBridgeProperties;
import org.amv.vertx.spring.eventbusbridge.EventBusBridgeVerticle;
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
        EventBusBridgeAutoConfigIT.TestApplictaion.class
})
public class EventBusBridgeAutoConfigIT {
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
    public void hasEventBusBridgeProperties() {
        final EventBusBridgeProperties bean = applicationContext.getBean(EventBusBridgeProperties.class);
        assertThat(bean, is(notNullValue()));
        assertThat(bean.getRoutePrefix(), is(Optional.of("eventbus")));
    }

    @Test
    public void hasEventBusBridgeVerticle() {
        final EventBusBridgeVerticle bean = applicationContext.getBean(EventBusBridgeVerticle.class);
        assertThat(bean, is(notNullValue()));
    }
}
