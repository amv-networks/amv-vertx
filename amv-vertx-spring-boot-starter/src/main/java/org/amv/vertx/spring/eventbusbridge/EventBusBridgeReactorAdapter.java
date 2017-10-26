package org.amv.vertx.spring.eventbusbridge;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.Json;
import io.vertx.core.streams.Pump;
import io.vertx.ext.reactivestreams.ReactiveReadStream;
import io.vertx.ext.reactivestreams.ReactiveWriteStream;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;

import static java.util.Objects.requireNonNull;

public class EventBusBridgeReactorAdapter<E> {
    private final Vertx vertx;
    private final EventBusBridgeEvents eventBusBridgeEvents;

    public EventBusBridgeReactorAdapter(Vertx vertx, EventBusBridgeEvents eventBusBridgeEvents) {
        this.vertx = requireNonNull(vertx);
        this.eventBusBridgeEvents = requireNonNull(eventBusBridgeEvents);
    }

    public <T extends E> void publish(Class<T> clazz, Publisher<T> publisher) {
        requireNonNull(clazz);
        requireNonNull(publisher);

        String eventName = eventBusBridgeEvents.toOutgoingEventName(clazz.getName());

        ReactiveReadStream<Object> rrs = ReactiveReadStream.readStream();

        Flux.from(publisher)
                .map(Json::encode)
                .retry()
                .subscribe(rrs);

        MessageProducer<Object> messageProducer = vertx.eventBus().publisher(eventName);

        Pump pump = Pump.pump(rrs, messageProducer);

        pump.start();
    }

    public <T extends E> void subscribe(Class<T> clazz, Subscriber<T> subscriber) {
        requireNonNull(clazz);
        requireNonNull(subscriber);

        String eventName = eventBusBridgeEvents.toIncomingEventName(clazz.getName());

        final MessageConsumer<String> consumer = vertx.eventBus().consumer(eventName);

        ReactiveWriteStream<String> rws = ReactiveWriteStream.writeStream(vertx);

        Flux.from(rws)
                .map(json -> Json.decodeValue(json, clazz))
                .retry()
                .subscribe(subscriber);

        Pump pump = Pump.pump(consumer.bodyStream(), rws);

        pump.start();
    }
}
