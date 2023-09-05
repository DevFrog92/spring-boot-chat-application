package com.example.chat.domain.common.Exception;

public class CustomNoSuchElementException extends CustomRuntimeException {
    public CustomNoSuchElementException() {
        super(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION.toString());
    }
}
