package io.goldexchange.exception;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException(String message) {
        super(message);
    }
}
