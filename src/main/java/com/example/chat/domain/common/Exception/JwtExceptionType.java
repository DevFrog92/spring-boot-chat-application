package com.example.chat.domain.common.Exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public enum JwtExceptionType {
    SIGNATURE("Invalid token signature"),
    EXPIRED("Expired token"),
    INVALID("Invalid token"),
    UNSUPPORTED("Unsupported JWT");

    private final String message;

    JwtExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static JwtExceptionType getExceptionTypeForRuntimeException(RuntimeException ex) {
        if (ex instanceof SignatureException) {
            return SIGNATURE;
        } else if (ex instanceof MalformedJwtException) {
            return INVALID;
        } else if (ex instanceof ExpiredJwtException) {
            return EXPIRED;
        } else if (ex instanceof UnsupportedJwtException) {
            return UNSUPPORTED;
        } else {
            return INVALID;
        }
    }
}
