package dev.sultanov.qraphql;

import com.google.api.graphql.execution.GuavaListenableFutureSupport;
import com.google.api.graphql.rejoiner.Schema;
import com.google.api.graphql.rejoiner.SchemaProviderModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import dev.sultanov.grpc.book.BookServiceGrpc;
import dev.sultanov.qraphql.client.GrpcClientModule;
import dev.sultanov.qraphql.dataloader.BookBatchLoader;
import dev.sultanov.qraphql.schema.GraphQlSchemaModule;
import graphql.execution.instrumentation.Instrumentation;
import graphql.schema.GraphQLSchema;
import graphql.servlet.config.DefaultGraphQLSchemaProvider;
import graphql.servlet.config.GraphQLSchemaProvider;
import graphql.servlet.context.DefaultGraphQLContext;
import graphql.servlet.context.DefaultGraphQLServletContext;
import graphql.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.servlet.context.GraphQLContext;
import graphql.servlet.context.GraphQLContextBuilder;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;

@SpringBootApplication
public class GraphQlApplication {

    private final Injector injector;

    // Initialize Guice modules before the Spring context
    {
        injector = Guice.createInjector(
                new SchemaProviderModule(),
                new GrpcClientModule(),
                new GraphQlSchemaModule()
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(GraphQlApplication.class, args);
    }

    @Bean
    public GraphQLSchemaProvider schemaProvider() {
        GraphQLSchema schema = injector.getInstance(Key.get(GraphQLSchema.class, Schema.class));
        return new DefaultGraphQLSchemaProvider(schema);
    }

    @Bean
    public Instrumentation instrumentation() {
        return GuavaListenableFutureSupport.listenableFutureInstrumentation();
    }

    @Bean
    public BookServiceGrpc.BookServiceFutureStub bookServiceFutureStub() {
        return injector.getInstance(BookServiceGrpc.BookServiceFutureStub.class);
    }

    @Bean
    public DataLoaderRegistry buildDataLoaderRegistry(BookBatchLoader bookBatchLoader) {
        DataLoaderRegistry registry = new DataLoaderRegistry();
        registry.register("books", new DataLoader<>(bookBatchLoader));
        return registry;
    }

    @Bean
    public GraphQLContextBuilder contextBuilder(DataLoaderRegistry dataLoaderRegistry) {
        return new GraphQLContextBuilder() {
            @Override
            public GraphQLContext build(HttpServletRequest req, HttpServletResponse response) {
                return DefaultGraphQLServletContext.createServletContext(dataLoaderRegistry, null).with(req).with(response).build();
            }

            @Override
            public GraphQLContext build() {
                return new DefaultGraphQLContext(dataLoaderRegistry, null);
            }

            @Override
            public GraphQLContext build(Session session, HandshakeRequest request) {
                return DefaultGraphQLWebSocketContext.createWebSocketContext(dataLoaderRegistry, null).with(session).with(request).build();
            }
        };
    }
}
