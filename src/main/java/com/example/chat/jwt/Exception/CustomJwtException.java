package com.example.chat.jwt.Exception;

public class CustomJwtException extends RuntimeException{
    public CustomJwtException() {
        super();
    }

    public CustomJwtException(Throwable cause) {
        super(cause);
    }
}
