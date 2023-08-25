package com.example.chat.global.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ResponseDto<T> {
    private final String message;
    private final T body;
}
