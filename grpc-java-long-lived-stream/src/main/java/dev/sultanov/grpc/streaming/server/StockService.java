package dev.sultanov.grpc.streaming.server;

import dev.sultanov.grpc.streaming.StockPriceRequest;
import dev.sultanov.grpc.streaming.StockPriceResponse;
import dev.sultanov.grpc.streaming.StockServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

public class StockService extends StockServiceGrpc.StockServiceImplBase {

    private final StockRepository repository = StockRepository.INSTANCE;

    @Override
    public void getPrice(StockPriceRequest request, StreamObserver<StockPriceResponse> responseObserver) {
        repository.getStock(request.getSymbol())
                .ifPresentOrElse(
                        stock -> {
                            responseObserver.onNext(StockPriceResponse.newBuilder().setSymbol(stock.getSymbol()).setPrice(stock.getPrice()).build());
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(new StatusException(Status.NOT_FOUND))
                );
    }
}
