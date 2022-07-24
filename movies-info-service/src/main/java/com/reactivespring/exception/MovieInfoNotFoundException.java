package com.reactivespring.exception;

public class MovieInfoNotFoundException extends RuntimeException {
    private final String message;

    public MovieInfoNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
