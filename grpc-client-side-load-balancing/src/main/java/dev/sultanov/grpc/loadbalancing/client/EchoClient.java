package dev.sultanov.grpc.loadbalancing.client;

import dev.sultanov.grpc.loadbalancing.EchoRequest;
import dev.sultanov.grpc.loadbalancing.EchoResponse;
import dev.sultanov.grpc.loadbalancing.EchoServiceGrpc;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolver;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EchoClient {

    public static void main(String[] args) {
        NameResolver.Factory nameResolverFactory = new MultiAddressNameResolverFactory(
                new InetSocketAddress("localhost", 50000),
                new InetSocketAddress("localhost", 50001),
                new InetSocketAddress("localhost", 50002)
        );

        ManagedChannel channel = ManagedChannelBuilder.forTarget("service")
                .nameResolverFactory(nameResolverFactory)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();

        EchoServiceGrpc.EchoServiceBlockingStub stub = EchoServiceGrpc.newBlockingStub(channel);
        for (int i = 0; i < 10; i++) {
            EchoResponse response = stub.echo(EchoRequest.newBuilder()
                    .setMessage("Hello!")
                    .build());
            System.out.println(response.getMessage());
        }
    }

    private static class MultiAddressNameResolverFactory extends NameResolver.Factory {

        final List<EquivalentAddressGroup> addresses;

        MultiAddressNameResolverFactory(SocketAddress... addresses) {
            this.addresses = Arrays.stream(addresses)
                    .map(EquivalentAddressGroup::new)
                    .collect(Collectors.toList());
        }

        public NameResolver newNameResolver(URI notUsedUri, NameResolver.Args args) {
            return new NameResolver() {

                @Override
                public String getServiceAuthority() {
                    return "fakeAuthority";
                }

                public void start(Listener2 listener) {
                    listener.onResult(ResolutionResult.newBuilder().setAddresses(addresses).setAttributes(Attributes.EMPTY).build());
                }

                public void shutdown() {
                }
            };
        }

        @Override
        public String getDefaultScheme() {
            return "multiaddress";
        }
    }
}
