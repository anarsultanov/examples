package dev.sultanov.grpc.streaming.server;

import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        DomainEvents.subscribe(new StockPriceChangedEventListener());
        Executors.newFixedThreadPool(1).submit(new RandomStockPriceUpdatingTask());
    }
}
