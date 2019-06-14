package dev.sultanov.grpc.authentication.server;

import dev.sultanov.grpc.authentication.shared.GreetingRequest;
import dev.sultanov.grpc.authentication.shared.GreetingResponse;
import dev.sultanov.grpc.authentication.shared.GreetingServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(8080)
                .addService(new GreetingService())
                .build();

        server.start();
        System.out.println("gRPC Server started, listening on port:" + server.getPort());
        server.awaitTermination();
    }

    private static class GreetingService extends GreetingServiceGrpc.GreetingServiceImplBase {

        @Override
        public void greeting(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
            String name = request.getName();
            String greeting = String.format("Hello, %s!", name.isBlank() ? "World" : name);
            GreetingResponse response = GreetingResponse.newBuilder()
                    .setGreeting(greeting)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

}
