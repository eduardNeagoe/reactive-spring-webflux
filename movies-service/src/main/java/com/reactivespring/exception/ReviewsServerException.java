package com.reactivespring.exception;

public class ReviewsServerException extends RuntimeException {
    private final String message;

    public ReviewsServerException(String message) {
        super(message);
        this.message = message;
    }
}
