package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.dto.message.ChatMessageDto;
import com.example.chat.domain.chatroom.dto.message.ChatRoomInfoDto;
import com.example.chat.domain.chatroom.dto.message.MemberTopicMessageDto;
import com.example.chat.domain.chatroom.service.port.ChatService;
import com.example.chat.domain.mock.FakeRedisPublish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.example.chat.domain.chatroom.dto.message.ChatMessageType.*;
import static org.assertj.core.api.Assertions.assertThat;

class ChatServiceImplTest {

    private ChatService chatService;
    private FakeRedisPublish fakeRedisPublish;

    @BeforeEach
    void init() {
        this.fakeRedisPublish = new FakeRedisPublish();
        this.chatService = ChatServiceImpl.builder()
                .redisPublish(this.fakeRedisPublish)
                .build();
    }

    @Test
    void sendChatMessage_메서드는_chatroom_토픽으로_메시지를_보낸다() {
        //given
        String topic = "chatroom";
        ChatMessageDto messageDto = ChatMessageDto.builder()
                .message("test message")
                .sender("memberA")
                .type(MESSAGE)
                .roomId(1L)
                .build();

        //when
        chatService.sendChatMessage(messageDto);

        Map<String, Object> result = fakeRedisPublish.getData(topic);
        ChatMessageDto message = (ChatMessageDto) result.get(topic);

        //then
        assertThat(message).isNotNull();
        assertThat(message.getMessage()).isEqualTo("test message");
        assertThat(message.getSender()).isEqualTo("memberA");
        assertThat(message.getType()).isEqualTo(MESSAGE);
        assertThat(message.getRoomId()).isEqualTo(1L);
    }

    @Test
    void sendEnterChatRoomMessage_메서드는_chatroom_토픽으로_메시지를_보내고_채팅방_id_와_참여자_이름을_받는다() {
        //given
        String topic = "chatroom";

        //when
        chatService.sendEnterChatRoomMessage(1L, "memberA");

        Map<String, Object> result = fakeRedisPublish.getData(topic);
        ChatMessageDto message = (ChatMessageDto) result.get(topic);

        //then
        assertThat(message).isNotNull();
        assertThat(message.getMessage()).isEqualTo("memberA 님이 참여했습니다.");
        assertThat(message.getSender()).isEqualTo("📣 알림");
        assertThat(message.getType()).isEqualTo(NOTICE);
        assertThat(message.getRoomId()).isEqualTo(1L);
    }

    @Test
    void sendLeaveChatRoomMessage_메서드는_chatroom_토픽으로_메시지를_보내고_채팅방_id_와_참여자_이름을_받는다() {
        //given
        String topic = "chatroom";

        //when
        chatService.sendLeaveChatRoomMessage(1L, "memberA");

        Map<String, Object> result = fakeRedisPublish.getData(topic);
        ChatMessageDto message = (ChatMessageDto) result.get(topic);

        //then
        assertThat(message).isNotNull();
        assertThat(message.getMessage()).isEqualTo("memberA 님이 방을 나갔습니다.");
        assertThat(message.getSender()).isEqualTo("📣 알림");
        assertThat(message.getType()).isEqualTo(NOTICE);
        assertThat(message.getRoomId()).isEqualTo(1L);
    }

    @Test
    void sendMessageToMemberTopic_메서드는_member_토픽으로_메시지를_보내고_참여자_id_와_메세지_타입을_받는다() {
        //given
        String topic = "member";

        //when
        chatService.sendMessageToMemberTopic(1L, BAN);

        Map<String, Object> result = fakeRedisPublish.getData(topic);
        MemberTopicMessageDto message = (MemberTopicMessageDto) result.get(topic);

        //then
        assertThat(message).isNotNull();
        assertThat(message.getType()).isEqualTo(BAN);
        assertThat(message.getMemberId()).isEqualTo(1L);
    }

    @Test
    void sendChatRoomDeleted_메서드는_chatroom_토픽으로_메시지를_보내고_채팅방_id_를_받는다() {
        //given
        String topic = "chatroom";

        //when
        chatService.sendChatRoomDeleted(1L);

        Map<String, Object> result = fakeRedisPublish.getData(topic);
        ChatMessageDto message = (ChatMessageDto) result.get(topic);

        //then
        assertThat(message).isNotNull();
        assertThat(message.getType()).isEqualTo(DELETE);
        assertThat(message.getRoomId()).isEqualTo(1L);
    }

    @Test
    void sendChatRoomInfo_메서드는_chatroomInfo_토픽으로_메시지를_보내고_채팅방_id_과_참여자수_를_받는다() {
        //given
        String topic = "chatroomInfo";

        //when
        chatService.sendChatRoomInfo(1L, 10);

        Map<String, Object> result = fakeRedisPublish.getData(topic);
        ChatRoomInfoDto message = (ChatRoomInfoDto) result.get(topic);

        //then
        assertThat(message).isNotNull();
        assertThat(message.getRoomId()).isEqualTo(1L);
        assertThat(message.getParticipationNum()).isEqualTo(10);
    }
}
