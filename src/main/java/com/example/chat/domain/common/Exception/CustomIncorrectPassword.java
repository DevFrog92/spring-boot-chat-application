package com.example.chat.domain.common.Exception;

public class CustomIncorrectPassword extends CustomRuntimeException {
    public CustomIncorrectPassword() {
        super(ExceptionCode.INCORRECT_PASSWORD.toString());
    }
}
