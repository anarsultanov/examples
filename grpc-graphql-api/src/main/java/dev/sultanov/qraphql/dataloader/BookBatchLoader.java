package dev.sultanov.qraphql.dataloader;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import dev.sultanov.grpc.book.Book;
import dev.sultanov.grpc.book.BookServiceGrpc;
import dev.sultanov.grpc.book.ListBooksRequest;
import net.javacrumbs.futureconverter.java8guava.FutureConverter;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component
public class BookBatchLoader implements BatchLoader<Integer, Book> {

    private final BookServiceGrpc.BookServiceFutureStub futureStub;

    public BookBatchLoader(BookServiceGrpc.BookServiceFutureStub futureStub) {
        this.futureStub = futureStub;
    }

    @Override
    public CompletionStage<List<Book>> load(List<Integer> keys) {
        ListenableFuture<List<Book>> listenableFuture =
                Futures.transform(futureStub.listBooks(ListBooksRequest.newBuilder().addAllIds(keys).build()),
                        listBooksResponse -> listBooksResponse != null ? listBooksResponse.getBooksList() : null,
                        MoreExecutors.directExecutor());
        return FutureConverter.toCompletableFuture(listenableFuture);
    }
}
