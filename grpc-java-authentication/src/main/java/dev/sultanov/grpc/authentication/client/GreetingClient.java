package dev.sultanov.grpc.authentication.client;

import dev.sultanov.grpc.authentication.shared.GreetingRequest;
import dev.sultanov.grpc.authentication.shared.GreetingResponse;
import dev.sultanov.grpc.authentication.shared.GreetingServiceGrpc;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        var stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingRequest request = GreetingRequest.newBuilder().setName("John").build();
        GreetingResponse response = stub.greeting(request);
        System.out.println(response.getGreeting());
    }
}
