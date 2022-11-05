package dev.sultanov.grpc.streaming.client;

import dev.sultanov.grpc.streaming.StockPriceRequest;
import dev.sultanov.grpc.streaming.StockPriceResponse;
import dev.sultanov.grpc.streaming.StockServiceGrpc;
import io.grpc.Context;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockServiceClient {

    private static final Logger logger = Logger.getLogger(StockServiceClient.class.getName());

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        var stub = StockServiceGrpc.newBlockingStub(channel);

        try (var cancellableContext = Context.current().withCancellation()) {
            var counter = new AtomicInteger();
            cancellableContext.run(() -> {
                var response = stub.getPrice(StockPriceRequest.newBuilder().setSymbol("AMZN").build());
                response.forEachRemaining(stockPriceResponse -> {
                    StockServiceClient.printStockPrice(stockPriceResponse);
                    if (counter.incrementAndGet() == 5) {
                        cancellableContext.cancel(null);
                    }
                });
            });
        } catch (StatusRuntimeException e) {
            var status = Status.fromThrowable(e);
            logger.log(Level.WARNING, "Cancelled with status: " + status);
        }

        System.out.println("Stock price retrieval completed!");
    }

    private static void printStockPrice(StockPriceResponse stockPriceResponse) {
        System.out.println("Stock: " + stockPriceResponse.getSymbol() + ", price: " + stockPriceResponse.getPrice());
    }
}
