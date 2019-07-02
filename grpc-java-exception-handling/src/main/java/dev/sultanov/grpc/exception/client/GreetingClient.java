package dev.sultanov.grpc.exception.client;

import dev.sultanov.grpc.exception.common.GreetingRequest;
import dev.sultanov.grpc.exception.common.GreetingResponse;
import dev.sultanov.grpc.exception.common.GreetingServiceGrpc;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        var stub = GreetingServiceGrpc.newBlockingStub(channel);

        GreetingRequest request = GreetingRequest.newBuilder().build();
        GreetingResponse response = stub.greeting(request);
        System.out.println(response.getGreeting());
    }
}
