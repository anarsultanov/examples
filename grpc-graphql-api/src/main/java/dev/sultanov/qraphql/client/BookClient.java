package dev.sultanov.qraphql.client;

import com.google.inject.AbstractModule;
import dev.sultanov.grpc.book.BookServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

final class BookClient extends AbstractModule {

    private static final String HOST = "localhost";
    private static final int PORT = 50002;

    @Override
    protected void configure() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();
        bind(BookServiceGrpc.BookServiceFutureStub.class).toInstance(BookServiceGrpc.newFutureStub(channel));
        bind(BookServiceGrpc.BookServiceBlockingStub.class).toInstance(BookServiceGrpc.newBlockingStub(channel));
    }
}
