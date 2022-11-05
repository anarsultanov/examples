package dev.sultanov.grpc.streaming.client;

import dev.sultanov.grpc.streaming.StockPriceRequest;
import dev.sultanov.grpc.streaming.StockPriceResponse;
import dev.sultanov.grpc.streaming.StockServiceGrpc;
import io.grpc.ManagedChannelBuilder;

public class StockServiceClient {

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        var stub = StockServiceGrpc.newBlockingStub(channel);

        var stockPriceResponseIterator = stub.getPrice(StockPriceRequest.newBuilder().setSymbol("AMZN").build());
        stockPriceResponseIterator.forEachRemaining(StockServiceClient::printStockPrice);
    }

    private static void printStockPrice(StockPriceResponse stockPriceResponse) {
        System.out.println("Stock: " + stockPriceResponse.getSymbol() + ", price: " + stockPriceResponse.getPrice());
    }
}
