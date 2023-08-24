package com.example.chat.service;

import com.example.chat.domain.ChatRoom;
import com.example.chat.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    public void sendChatMessage(ChatMessageDto chatMessage) {
        if (ChatMessageType.JOIN.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");
        } else if (ChatMessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 퇴장하셨습니다.");
            chatMessage.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    public void sendBanMessage(ChatBanDto dto) {
        redisTemplate.convertAndSend("banMember", dto);
    }

    public void sendDeletedChatRoom(ChatDeleteDto dto) {
        redisTemplate.convertAndSend("deleteChatRoom", dto);
    }

    public void sendUpdateChatRoomInfo(ChatRoomInfoDto dto) {
        redisTemplate.convertAndSend("updateChatRoomInfo", dto);
    }
}
