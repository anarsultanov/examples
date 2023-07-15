package dev.sultanov.grpc.streaming.server;

public record StockPriceChangedEvent(String symbol, double price) {

}
