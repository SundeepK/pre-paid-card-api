package com.curve.exception;

public class InvalidNonceException extends Exception {

    public InvalidNonceException(String message) {
        super(message);
    }

    public InvalidNonceException(String message, Throwable cause) {
        super(message, cause);
    }
}
