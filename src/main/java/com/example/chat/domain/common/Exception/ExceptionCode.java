package com.example.chat.domain.common.Exception;

public enum ExceptionCode {
    INCORRECT_PASSWORD("채팅방의 입장 코드가 맞지 않습니다."),
    NO_SUCH_ELEMENT_EXCEPTION("요청한 데이터가 존재하지 않습니다."),
    NOT_ALLOW_ACCESS("잘못된 접근 시도입니다.");

    private final String message;

    ExceptionCode(String description) {
        this.message = description;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
