package com.example.chat.domain.chatroom.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestBanMemberDto {
    private Long roomId;
    private Long requestMemberId;
    private String banMemberName;
    private Long banMemberId;

    @Builder
    public RequestBanMemberDto(Long roomId,
                               Long requestMemberId,
                               String banMemberName,
                               Long banMemberId) {
        this.roomId = roomId;
        this.requestMemberId = requestMemberId;
        this.banMemberName = banMemberName;
        this.banMemberId = banMemberId;
    }
}
