package com.example.chat.domain.common.Exception.handler;

import com.example.chat.domain.common.Exception.*;
import com.example.chat.domain.common.controller.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler({CustomNoSuchElementException.class})
    public ResponseEntity<?> handleNoSuchElementException(
            CustomRuntimeException ex) {
        return new ResponseEntity<>(
                new ResponseDto<>(
                        ex.getMessage(),
                        null
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({CustomOutOfPermissionException.class})
    public ResponseEntity<?> handlePermissionException(
            CustomRuntimeException ex) {
        return new ResponseEntity<>(
                new ResponseDto<>(
                        ex.getMessage(),
                        null
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler({CustomMessageSendException.class})
    public ResponseEntity<?> handleMessageSendException(
            CustomRuntimeException ex) {
        return new ResponseEntity<>(
                new ResponseDto<>(
                        ex.getMessage(),
                        null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({CustomRuntimeException.class})
    public ResponseEntity<?> handleCustomRuntimeException(
            CustomRuntimeException ex) {
        return new ResponseEntity<>(
                new ResponseDto<>(
                        ex.getMessage(),
                        null
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(
            CustomRuntimeException ex) {
        return new ResponseEntity<>(
                new ResponseDto<>(
                        "잠시 시스템에 문제가 발생했습니다",
                        null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
