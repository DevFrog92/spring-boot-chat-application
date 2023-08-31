package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.member.domain.Member;
import org.junit.jupiter.api.Test;

import static com.example.chat.domain.chatroom.domain.ChatRoomType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;

class ChatRoomTest {

    @Test
    void ChatRoom_은_Member_와_ChatRoomCreate_객체로_생성할_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatroomA = ChatRoomCreate.builder()
                .requestMemberId(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .secretKey("asde#$%")
                .maxPeopleAllowNum(100)
                .build();

        //when
        ChatRoom chatRoom = ChatRoom.create(member, chatroomA);

        //then
        assertThat(chatRoom.getName()).isEqualTo("chatroomA");
        assertThat(chatRoom.getType()).isEqualTo(PUBLIC);
        assertThat(chatRoom.getSecretCode()).isEqualTo("asde#$%");
        assertThat(chatRoom.getMaxChatRoomSize()).isEqualTo(100);
        assertThat(chatRoom.getParticipationNum()).isEqualTo(0);
        assertThat(chatRoom.getMember()).isEqualTo(member);
    }

    @Test
    void ChatRoom_은_getOwnerId_메서드로_채팅방_관리자의_id_를_반환할_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatroomA = ChatRoomCreate.builder()
                .build();

        ChatRoom chatRoom = ChatRoom.create(member, chatroomA);

        //when
        Long ownerId = chatRoom.getOwnerId();

        //then
        assertThat(ownerId).isEqualTo(1L);
    }

    @Test
    void ChatRoom_은_join_메서드로_참가인원이_1_증가한_채팅방_참여객체를_생성할_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatroomA = ChatRoomCreate.builder()
                .build();

        ChatRoom chatRoom = ChatRoom.create(member, chatroomA);

        //when
        chatRoom = chatRoom.join();

        //then
        assertThat(chatRoom.getParticipationNum()).isEqualTo(1);
    }

    @Test
    void ChatRoom_은_leave_메서드로_참가인원이_1_감소한_채팅방_참여객체를_생성할_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatroomA = ChatRoomCreate.builder()
                .build();

        ChatRoom chatRoom = ChatRoom.create(member, chatroomA);

        //when
        chatRoom = chatRoom.join(); // increase participationNum
        chatRoom = chatRoom.leave();

        //then
        assertThat(chatRoom.getParticipationNum()).isEqualTo(0);
    }

    @Test
    void ChatRoom_은_leave_메서드는_참여인원을_0_이하로_감소시킬_수_없다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatroomA = ChatRoomCreate.builder()
                .build();

        ChatRoom chatRoom = ChatRoom.create(member, chatroomA);

        //when
        chatRoom = chatRoom.leave();

        //then
        assertThat(chatRoom.getParticipationNum()).isEqualTo(0);
    }

    @Test
    void ChatRoom_은_checkCode_메서드로_파라미터로_넘긴_코드와_채팅방의_코드와_비교하고_불린값을_반환한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatroomA = ChatRoomCreate.builder()
                .secretKey("asde#$%")
                .build();

        ChatRoom chatRoom = ChatRoom.create(member, chatroomA);

        //when
        boolean result = chatRoom.checkCode("asde#$%");
        //then
        assertThat(result).isTrue();
    }
}
