package com.example.chat.domain.common.domain.Exception;

public class CustomJwtException extends RuntimeException {
    public CustomJwtException() {
        super();
    }

    public CustomJwtException(Throwable cause) {
        super(cause);
    }
}
