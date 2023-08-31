package com.example.chat.domain.common.domain.Exception;

public class CustomNoSuchElementException extends CustomRuntimeException {
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
