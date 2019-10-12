package dev.sultanov.grpc.author;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class AuthorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(50001)
                .addService(new AuthorService())
                .build();

        server.start();
        System.out.println("gRPC Server started, listening on port:" + server.getPort());
        server.awaitTermination();
    }
}
