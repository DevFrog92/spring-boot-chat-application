package com.example.chat.config.handler;

import com.example.chat.dto.ChatMessageDto;
import com.example.chat.dto.ChatMessageType;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.service.ChatService;
import com.example.chat.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

    // redis 에 발행하는 로직을 서비스로 위임

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // 메시지를 보낼 때, token을 검증한다.
        if(StompCommand.CONNECT == accessor.getCommand()) {
            String jwtToken = accessor.getFirstNativeHeader("token");
            log.info("CONNECT {}", jwtToken);
            jwtProvider.validateToken(jwtToken);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            String destination = Optional.ofNullable((String) message
                            .getHeaders()
                            .get("simpDestination")
                    ).orElse("InvalidRoomId");

            String roomId = chatService.getRoomId(destination);

            String sessionId = (String) message.getHeaders().get("simpSessionId");

            chatRoomRepository.setUserEnterInfo(sessionId, roomId);
            chatRoomRepository.plusUserCount(roomId);

            String name = Optional.ofNullable((Principal) message
                            .getHeaders().get("simpUser"))
                    .map(Principal::getName).orElse("UnknownUser");

            ChatMessageDto chatMessageDto = new ChatMessageDto();
            chatMessageDto.setSender(name);
            chatMessageDto.setRoomId(roomId);
            chatMessageDto.setType(ChatMessageType.JOIN);
            chatService.sendChatMessage(chatMessageDto);
            log.info("SUBSCRIBE {}, {}", name, roomId);

        }else if(StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);

            chatRoomRepository.minusUserCount(roomId);
            String name = Optional.ofNullable((Principal) message
                            .getHeaders()
                            .get("simpUser"))
                    .map(Principal::getName).orElse("UnknownUser");
            ChatMessageDto chatMessageDto = new ChatMessageDto();
            chatMessageDto.setSender(name);
            chatMessageDto.setRoomId(roomId);
            chatMessageDto.setType(ChatMessageType.QUIT);
            chatService.sendChatMessage(chatMessageDto);
            chatRoomRepository.removeUserEnterInfo(sessionId);
            log.info("DISCONNECT {}, {}", name, roomId);
        }else {
            log.info("What?");
        }
        log.info("handler finish");
        return message;
    }


}
