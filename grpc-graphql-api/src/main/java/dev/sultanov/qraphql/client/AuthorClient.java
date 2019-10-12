package dev.sultanov.qraphql.client;

import com.google.inject.AbstractModule;
import dev.sultanov.grpc.author.AuthorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

final class AuthorClient extends AbstractModule {

    private static final String HOST = "localhost";
    private static final int PORT = 50001;

    @Override
    protected void configure() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();
        bind(AuthorServiceGrpc.AuthorServiceFutureStub.class).toInstance(AuthorServiceGrpc.newFutureStub(channel));
        bind(AuthorServiceGrpc.AuthorServiceBlockingStub.class).toInstance(AuthorServiceGrpc.newBlockingStub(channel));
    }
}
