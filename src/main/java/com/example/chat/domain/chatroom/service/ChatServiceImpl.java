package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.dto.message.ChatMessageDto;
import com.example.chat.domain.chatroom.dto.message.ChatMessageType;
import com.example.chat.domain.chatroom.dto.message.ChatRoomInfoDto;
import com.example.chat.domain.chatroom.dto.message.MemberTopicMessageDto;
import com.example.chat.domain.chatroom.service.port.ChatService;
import com.example.chat.domain.common.service.port.RedisPublish;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.chat.domain.chatroom.dto.message.ChatMessageType.DELETE;
import static com.example.chat.domain.chatroom.dto.message.ChatMessageType.NOTICE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final RedisPublish redisPublish;

    @Override
    public void sendChatMessage(ChatMessageDto dto) {
        redisPublish.publish("chatroom", dto);
    }

    @Override
    public void sendEnterChatRoomMessage(Long roomId, String name) {
        redisPublish.publish("chatroom",
                ChatMessageDto.builder()
                        .type(NOTICE)
                        .roomId(roomId)
                        .sender("📣 알림")
                        .message(name + " 님이 참여했습니다.")
                        .build()
        );
    }

    @Override
    public void sendLeaveChatRoomMessage(Long roomId, String name) {
        redisPublish.publish("chatroom",
                ChatMessageDto.builder()
                        .type(NOTICE)
                        .roomId(roomId)
                        .sender("📣 알림")
                        .message(name + " 님이 방을 나갔습니다.")
                        .build()
        );
    }

    @Override
    public void sendMessageToMemberTopic(Long memberId, ChatMessageType type) {
        redisPublish.publish("member",
                MemberTopicMessageDto.builder()
                        .type(type)
                        .memberId(memberId)
                        .build()
        );
    }

    @Override
    public void sendChatRoomDeleted(Long roomId) {
        redisPublish.publish("chatroom",
                ChatMessageDto.builder()
                        .type(DELETE)
                        .roomId(roomId)
                        .build()
        );

    }

    @Override
    public void sendChatRoomInfo(Long roomId, Integer participationNum) {
        redisPublish.publish("chatroomInfo",
                ChatRoomInfoDto.builder()
                        .roomId(roomId)
                        .participationNum(participationNum)
                        .build()
        );
    }
}
