package dev.sultanov.grpc.streaming.client;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import dev.sultanov.grpc.webflux.StockPriceRequest;
import dev.sultanov.grpc.webflux.StockPriceResponse;
import dev.sultanov.grpc.webflux.StockServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class StockApplication {

    private final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
            .usePlaintext()
            .build();
    private final StockServiceGrpc.StockServiceStub stub = StockServiceGrpc.newStub(channel);

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> getStockPrice() {
        return route(GET("/stocks/{symbol}"),
                req -> ServerResponse.ok().body(getStockPrice(req.pathVariable("symbol")), StockPrice.class));
    }

    private Flux<StockPrice> getStockPrice(String symbol) {
        var observer = new ReactiveStreamObserver<StockPrice, StockPriceResponse>() {
            @Override
            public StockPrice process(StockPriceResponse value) {
                return new StockPrice(value.getSymbol(), value.getPrice());
            }
        };
        StockPriceRequest request = StockPriceRequest.newBuilder().setSymbol(symbol).build();
        stub.getPrice(request, observer);
        return observer.getFlux();
    }

    private record StockPrice(String symbol, double price) {

    }
}
