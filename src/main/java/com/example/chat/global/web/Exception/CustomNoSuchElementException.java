package com.example.chat.global.web.Exception;

public class CustomNoSuchElementException extends RuntimeException {
    public CustomNoSuchElementException() {
        super();
    }

    public CustomNoSuchElementException(String message) {
        super(message);
    }

    public CustomNoSuchElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomNoSuchElementException(Throwable cause) {
        super(cause);
    }
}
