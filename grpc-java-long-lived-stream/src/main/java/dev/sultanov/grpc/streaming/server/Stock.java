package dev.sultanov.grpc.streaming.server;

public class Stock {

    private final String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void updatePrice(double price) {
        this.price = price;
        DomainEvents.publish(new StockPriceChangedEvent(this.symbol, this.price));
    }
}
