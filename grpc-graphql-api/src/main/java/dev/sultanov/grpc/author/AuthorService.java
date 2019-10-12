package dev.sultanov.grpc.author;

import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthorService extends AuthorServiceGrpc.AuthorServiceImplBase {

    private final AtomicInteger authorIdCounter = new AtomicInteger(1);
    private final Map<Integer, Author> authorsById = new HashMap<>();

    public void createAuthor(CreateAuthorRequest request, StreamObserver<Author> responseObserver) {
        int id = authorIdCounter.getAndIncrement();
        Author author = Author.newBuilder()
                .setId(id)
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .build();
        authorsById.put(id, author);
        responseObserver.onNext(author);
        responseObserver.onCompleted();
    }

    public void getAuthor(GetAuthorRequest request, StreamObserver<Author> responseObserver) {
        responseObserver.onNext(authorsById.get(request.getId()));
        responseObserver.onCompleted();
    }

    public void addBook(AddBookRequest request, StreamObserver<Author> responseObserver) {
        int authorId = request.getAuthorId();
        Author author = authorsById.get(authorId).toBuilder()
                .addBookIds(request.getBookId())
                .build();
        authorsById.put(authorId, author);
        responseObserver.onNext(author);
        responseObserver.onCompleted();
    }
}
