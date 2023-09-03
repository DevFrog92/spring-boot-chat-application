package com.example.chat.domain.chatroom.dto.message;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberTopicMessageDto implements ChatMessage {
    private ChatMessageType type;
    private Long memberId;

    @Builder
    public MemberTopicMessageDto(ChatMessageType type, Long memberId) {
        this.type = type;
        this.memberId = memberId;
    }
}
