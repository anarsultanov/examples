package dev.sultanov.grpc.streaming.server;

import dev.sultanov.grpc.streaming.StockPriceResponse;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum StockPriceChangedSubject {
    INSTANCE;

    private final Logger logger = Logger.getLogger(StockPriceChangedSubject.class.getName());
    private final Map<String, List<StreamObserver<StockPriceResponse>>> observers = new HashMap<>();
    private final Object MUTEX = new Object();

    public void register(String symbol, StreamObserver<StockPriceResponse> observer) {
        synchronized (MUTEX) {
            observers.computeIfAbsent(symbol, k -> new ArrayList<>()).add(observer);
        }
    }

    public void unregister(String symbol, StreamObserver<StockPriceResponse> observer) {
        synchronized (MUTEX) {
            Optional.ofNullable(observers.get(symbol)).ifPresent(observerList -> observerList.remove(observer));
        }
    }

    public void notify(StockPriceChangedEvent event) {
        List<StreamObserver<StockPriceResponse>> observerList;
        synchronized (MUTEX) {
            observerList = List.copyOf(this.observers.getOrDefault(event.symbol(), List.of()));
        }

        logger.log(Level.INFO, "Notifying {0} observers", observerList.size());
        var stockPrice = StockPriceResponse.newBuilder().setSymbol(event.symbol()).setPrice(event.price()).build();
        observerList.forEach(observer -> {
            try {
                observer.onNext(stockPrice);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error notifying observer", e);
            }
        });
    }
}
