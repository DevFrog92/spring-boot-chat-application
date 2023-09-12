package com.example.chat.domain.chatroom.dto.message;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTopicMessageDto implements ChatMessage {
    private ChatMessageType type;
    private Long memberId;

    @Builder
    public MemberTopicMessageDto(final ChatMessageType type,
                                 final Long memberId) {
        this.type = type;
        this.memberId = memberId;
    }
}
