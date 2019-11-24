package dev.sultanov.grpc.loadbalancing.server;

import dev.sultanov.grpc.loadbalancing.EchoRequest;
import dev.sultanov.grpc.loadbalancing.EchoResponse;
import dev.sultanov.grpc.loadbalancing.EchoServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServers {

    public static void main(String[] args) {
        final int nServers = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(nServers);
        for (int i = 0; i < nServers; i++) {
            String name = "Server_" + i;
            int port = 50000 + i;
            executorService.submit(() -> {
                try {
                    startServer(name, port);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void startServer(String name, int port) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(port)
                .addService(new EchoService(name))
                .build();

        server.start();
        System.out.println(name + " server started, listening on port: " + server.getPort());
        server.awaitTermination();
    }

    private static class EchoService extends EchoServiceGrpc.EchoServiceImplBase {

        private final String name;

        EchoService(String name) {
            this.name = name;
        }

        @Override
        public void echo(EchoRequest request, StreamObserver<EchoResponse> responseObserver) {
            String reply = this.name + " echo: " + request.getMessage();
            EchoResponse response = EchoResponse.newBuilder()
                    .setMessage(reply)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
