package dev.sultanov.qraphql.client;

import com.google.inject.AbstractModule;

public final class GrpcClientModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new BookClient());
        install(new AuthorClient());
    }
}
