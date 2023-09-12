package com.example.chat.domain.chatroom.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestSubmitCodeDto {
    private Long memberId;
    private Long roomId;
    private String secretCode;

    @Builder
    public RequestSubmitCodeDto(
            @JsonProperty("memberId") Long memberId,
            @JsonProperty("roomId") Long roomId,
            @JsonProperty("secretCode") String secretCode) {
        this.memberId = memberId;
        this.roomId = roomId;
        this.secretCode = secretCode;
    }
}
