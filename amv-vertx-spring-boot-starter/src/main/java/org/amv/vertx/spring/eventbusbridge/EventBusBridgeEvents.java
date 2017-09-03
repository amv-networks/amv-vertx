package org.amv.vertx.spring.eventbusbridge;

import static java.util.Objects.requireNonNull;

public class EventBusBridgeEvents {
    private static String INCOMING_EVENT_NAME_PREFIX = "bridge:in:";
    private static String OUTGOING_EVENT_NAME_PREFIX = "bridge:out:";

    public static String toIncomingEventName(String eventName) {
        requireNonNull(eventName);
        return INCOMING_EVENT_NAME_PREFIX + eventName;
    }

    public static String toOutgoingEventName(String eventName) {
        requireNonNull(eventName);
        return OUTGOING_EVENT_NAME_PREFIX + eventName;
    }
}
