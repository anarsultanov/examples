package dev.sultanov.grpc.streaming.server;

import com.google.common.eventbus.EventBus;

public class DomainEvents {

    private static final EventBus eventBus = new EventBus();

    private DomainEvents() {
        throw new AssertionError();
    }

    public static void publish(Object event) {
        eventBus.post(event);
    }

    public static void subscribe(Object eventListener) {
        eventBus.register(eventListener);
    }
}
