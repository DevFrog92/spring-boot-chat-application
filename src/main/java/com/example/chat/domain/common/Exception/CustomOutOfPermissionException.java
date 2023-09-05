package com.example.chat.domain.common.Exception;

public class CustomOutOfPermissionException extends CustomRuntimeException {
    public CustomOutOfPermissionException() {
        super(ExceptionCode.OUT_OF_PERMISSION.toString());
    }
}
