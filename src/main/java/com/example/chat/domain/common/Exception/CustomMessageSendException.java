package com.example.chat.domain.common.Exception;

public class CustomMessageSendException extends CustomRuntimeException {
    public CustomMessageSendException() {
        super(ExceptionCode.MESSAGE_SEND_FAIL.toString());
    }
}
