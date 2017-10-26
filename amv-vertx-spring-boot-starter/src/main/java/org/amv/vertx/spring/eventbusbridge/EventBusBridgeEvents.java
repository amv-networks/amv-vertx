package org.amv.vertx.spring.eventbusbridge;

import static java.util.Objects.requireNonNull;

public class EventBusBridgeEvents {
    private String incomingEventPrefix;
    private String outgoingEventPrefix;

    public EventBusBridgeEvents(String incomingEventPrefix, String outgoingEventPrefix) {
        this.incomingEventPrefix = requireNonNull(incomingEventPrefix);
        this.outgoingEventPrefix = requireNonNull(outgoingEventPrefix);
    }

    public String toIncomingEventName(String eventName) {
        requireNonNull(eventName);

        return incomingEventPrefix + eventName;
    }

    public String toOutgoingEventName(String eventName) {
        requireNonNull(eventName);

        return outgoingEventPrefix + eventName;
    }
}
