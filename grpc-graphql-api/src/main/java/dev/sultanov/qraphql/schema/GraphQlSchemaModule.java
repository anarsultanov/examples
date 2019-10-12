package dev.sultanov.qraphql.schema;

import com.google.inject.AbstractModule;

public class GraphQlSchemaModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new BookSchemaModule());
        install(new AuthorSchemaModule());
    }
}
