package com.example.chat.domain.common.Exception;

public class CustomJwtException extends RuntimeException {
    public CustomJwtException() {
        super();
    }

    public CustomJwtException(Throwable cause) {
        super(cause);
    }
}
