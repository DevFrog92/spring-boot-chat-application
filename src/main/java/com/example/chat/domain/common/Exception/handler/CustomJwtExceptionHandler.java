package com.example.chat.domain.common.Exception.handler;

import com.example.chat.domain.common.Exception.CustomJwtException;
import com.example.chat.domain.common.Exception.JwtExceptionType;
import com.example.chat.domain.common.controller.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomJwtExceptionHandler {

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<?> handleJwtException(CustomJwtException ex) {

        JwtExceptionType type = JwtExceptionType
                .getExceptionTypeForRuntimeException(ex);

        return new ResponseEntity<>(
                new ResponseDto<>(
                        type.getMessage(),
                        type
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
