package dev.sultanov.qraphql.schema;

import com.google.api.graphql.rejoiner.Mutation;
import com.google.api.graphql.rejoiner.Query;
import com.google.api.graphql.rejoiner.SchemaModification;
import com.google.api.graphql.rejoiner.SchemaModule;
import com.google.api.graphql.rejoiner.Type;
import com.google.api.graphql.rejoiner.TypeModification;
import com.google.common.util.concurrent.ListenableFuture;
import dev.sultanov.grpc.author.AddBookRequest;
import dev.sultanov.grpc.author.Author;
import dev.sultanov.grpc.author.AuthorServiceGrpc;
import dev.sultanov.grpc.author.CreateAuthorRequest;
import dev.sultanov.grpc.author.GetAuthorRequest;
import dev.sultanov.grpc.book.Book;
import graphql.schema.DataFetchingEnvironment;
import net.javacrumbs.futureconverter.java8guava.FutureConverter;

import java.util.List;

final class AuthorSchemaModule extends SchemaModule {

    @Mutation("createAuthor")
    ListenableFuture<Author> createAuthor(CreateAuthorRequest request, AuthorServiceGrpc.AuthorServiceFutureStub stub) {
        return stub.createAuthor(request);
    }

    @Query("getAuthor")
    ListenableFuture<Author> getAuthor(GetAuthorRequest request, AuthorServiceGrpc.AuthorServiceFutureStub stub) {
        return stub.getAuthor(request);
    }

    @Mutation("addBook")
    ListenableFuture<Author> addBook(AddBookRequest request, AuthorServiceGrpc.AuthorServiceFutureStub stub) {
        return stub.addBook(request);
    }

    @SchemaModification
    TypeModification removeBookIds = Type.find(Author.getDescriptor()).removeField("bookIds");

    @SchemaModification(addField = "books", onType = Author.class)
    ListenableFuture<List<Book>> authorToBooks(Author author, DataFetchingEnvironment environment) {
        return FutureConverter.toListenableFuture(
                environment.<Integer, Book>getDataLoader("books").loadMany(author.getBookIdsList())
        );
    }
}
