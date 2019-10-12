package dev.sultanov.grpc.book;

import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BookService extends BookServiceGrpc.BookServiceImplBase {

    private final AtomicInteger bookIdCounter = new AtomicInteger(1);
    private final Map<Integer, Book> booksById = new HashMap<>();

    public void createBook(CreateBookRequest request, StreamObserver<Book> responseObserver) {
        int id = bookIdCounter.getAndIncrement();
        Book book = Book.newBuilder()
                .setId(id)
                .setTitle(request.getTitle())
                .setAuthorId(request.getAuthorId())
                .build();
        booksById.put(id, book);
        responseObserver.onNext(book);
        responseObserver.onCompleted();
    }

    public void getBook(GetBookRequest request, StreamObserver<Book> responseObserver) {
        responseObserver.onNext(booksById.get(request.getId()));
        responseObserver.onCompleted();
    }

    public void listBooks(ListBooksRequest request, StreamObserver<ListBooksResponse> responseObserver) {
        ListBooksResponse.Builder builder = ListBooksResponse.newBuilder();
        request.getIdsList().forEach(id -> {
            builder.addBooks(booksById.get(id));
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
