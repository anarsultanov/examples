package dev.sultanov.grpc.streaming.client;

import io.grpc.stub.StreamObserver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

public abstract class ReactiveStreamObserver<T, S> implements StreamObserver<S> {

    private final Many<T> sink;

    protected ReactiveStreamObserver() {
        this.sink = Sinks.many().unicast().onBackpressureBuffer();
    }

    @Override
    public void onNext(S value) {
        sink.tryEmitNext(process(value));
    }

    @Override
    public void onError(Throwable t) {
        sink.tryEmitError(t);
    }

    @Override
    public void onCompleted() {
        sink.tryEmitComplete();
    }

    public Flux<T> getFlux() {
        return sink.asFlux();
    }

    public abstract T process(S value);

}
