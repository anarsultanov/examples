package dev.sultanov.grpc.book;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class BookServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(50002)
                .addService(new BookService())
                .build();

        server.start();
        System.out.println("gRPC Server started, listening on port:" + server.getPort());
        server.awaitTermination();
    }
}
