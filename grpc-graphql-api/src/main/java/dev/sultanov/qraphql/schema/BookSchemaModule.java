package dev.sultanov.qraphql.schema;

import com.google.api.graphql.rejoiner.Mutation;
import com.google.api.graphql.rejoiner.Query;
import com.google.api.graphql.rejoiner.SchemaModule;
import com.google.common.util.concurrent.ListenableFuture;
import dev.sultanov.grpc.author.AddBookRequest;
import dev.sultanov.grpc.author.Author;
import dev.sultanov.grpc.author.AuthorServiceGrpc;
import dev.sultanov.grpc.book.Book;
import dev.sultanov.grpc.book.BookServiceGrpc;
import dev.sultanov.grpc.book.CreateBookRequest;
import dev.sultanov.grpc.book.GetBookRequest;

final class BookSchemaModule extends SchemaModule {

    @Mutation("createBook")
    Book createBook(CreateBookRequest request, BookServiceGrpc.BookServiceBlockingStub bookStub,
                    AuthorServiceGrpc.AuthorServiceBlockingStub authorStub) {
        Book book = bookStub.createBook(request);
        Author author = authorStub.addBook(AddBookRequest.newBuilder()
                .setAuthorId(request.getAuthorId())
                .setBookId(book.getId()).build());
        return book;
    }

    @Query("getBook")
    ListenableFuture<Book> getBook(GetBookRequest request, BookServiceGrpc.BookServiceFutureStub stub) {
        return stub.getBook(request);
    }
}
