package dev.sultanov.qraphql;

import com.google.api.graphql.execution.GuavaListenableFutureSupport;
import com.google.api.graphql.rejoiner.Schema;
import com.google.api.graphql.rejoiner.SchemaProviderModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import dev.sultanov.qraphql.client.GrpcClientModule;
import dev.sultanov.qraphql.schema.GraphQlSchemaModule;
import graphql.execution.instrumentation.Instrumentation;
import graphql.schema.GraphQLSchema;
import graphql.servlet.config.DefaultGraphQLSchemaProvider;
import graphql.servlet.config.GraphQLSchemaProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
}
