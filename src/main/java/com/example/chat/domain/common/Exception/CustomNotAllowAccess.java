package com.example.chat.domain.common.Exception;

public class CustomNotAllowAccess extends CustomRuntimeException {
    public CustomNotAllowAccess() {
        super(ExceptionCode.NOT_ALLOW_ACCESS.toString());
    }
}
