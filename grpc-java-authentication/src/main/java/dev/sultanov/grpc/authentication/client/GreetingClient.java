package dev.sultanov.grpc.authentication.client;

import dev.sultanov.grpc.authentication.shared.Constants;
import dev.sultanov.grpc.authentication.shared.GreetingRequest;
import dev.sultanov.grpc.authentication.shared.GreetingResponse;
import dev.sultanov.grpc.authentication.shared.GreetingServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class GreetingClient {

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        BearerToken token = new BearerToken(getJwt());
        var stub = GreetingServiceGrpc.newBlockingStub(channel)
                .withCallCredentials(token);

        GreetingRequest request = GreetingRequest.newBuilder().setName("John").build();
        GreetingResponse response = stub.greeting(request);
        System.out.println(response.getGreeting());
    }

    private static String getJwt() {
        return Jwts.builder()
                .setSubject("GreetingClient")
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_SIGNING_KEY)
                .compact();
    }
}
