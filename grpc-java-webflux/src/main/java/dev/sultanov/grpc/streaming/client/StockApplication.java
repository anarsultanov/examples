package dev.sultanov.grpc.streaming.client;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class StockApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> getStockPrice() {
        return route(GET("/stocks/{symbol}"),
                req -> ServerResponse.ok().body(getStockPrice(req.pathVariable("symbol")), StockPrice.class));
    }

    private Flux<StockPrice> getStockPrice(String symbol) {
        return Flux.empty();
    }

    private record StockPrice(String symbol, double price) {

    }
}
