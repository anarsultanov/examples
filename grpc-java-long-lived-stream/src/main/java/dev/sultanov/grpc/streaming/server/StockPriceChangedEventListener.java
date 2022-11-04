package dev.sultanov.grpc.streaming.server;

import com.google.common.eventbus.Subscribe;

public class StockPriceChangedEventListener {

    @Subscribe
    public void handleEvent(StockPriceChangedEvent event) {
        System.out.println(event.toString());
    }
}
