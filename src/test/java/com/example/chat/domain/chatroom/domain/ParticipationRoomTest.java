package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.common.domain.Exception.CustomRuntimeException;
import com.example.chat.domain.member.domain.Member;
import org.junit.jupiter.api.Test;

import static com.example.chat.domain.chatroom.domain.ChatRoomType.PRIVATE;
import static com.example.chat.domain.chatroom.domain.ChatRoomType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParticipationRoomTest {
    @Test
    void create_메서드는_Member_와_ChatRoom_객체를_파라미터로_받아_ParticipationRoom_객체를_생성할_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .maxChatRoomSize(100)
                .member(member)
                .build();

        //when
        ParticipationRoom participationRoom = ParticipationRoom.create(member, chatroom);

        //then
        assertThat(participationRoom.getJoined()).isFalse();
        assertThat(participationRoom.getSubmitKey()).isTrue();
        assertThat(participationRoom.getMember()).isEqualTo(member);
        assertThat(participationRoom.getChatRoom()).isEqualTo(chatroom);
    }

    @Test
    void create_메서드의_파라미터인_chatRoom_의_type_이_PUBLIC_이면_submitKey_의_값은_true_이다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .type(PUBLIC)
                .build();

        //when
        ParticipationRoom participationRoom = ParticipationRoom.create(member, chatroom);

        //then
        assertThat(participationRoom.getSubmitKey()).isTrue();
    }

    @Test
    void create_메서드의_파라미터인_chatRoom_의_type_이_PRIVATE_이면_submitKey_의_값은_false_이다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .type(PRIVATE)
                .build();

        //when
        ParticipationRoom participationRoom = ParticipationRoom.create(member, chatroom);

        //then
        assertThat(participationRoom.getSubmitKey()).isFalse();
    }

    @Test
    void join_메서드는_Member_와_ChatRoom_객체를_파라미터로_받아_join_이_true_인_객체를_생성한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .maxChatRoomSize(100)
                .participationNum(0)
                .member(member)
                .build();

        ParticipationRoom participationRoom = ParticipationRoom.create(member, chatroom);

        //when
        participationRoom = participationRoom.join(member, chatroom);

        //then
        assertThat(participationRoom.getJoined()).isTrue();
        assertThat(participationRoom.getSubmitKey()).isTrue();
        assertThat(participationRoom.getMember()).isEqualTo(member);
        assertThat(participationRoom.getChatRoom().getId()).isEqualTo(1L);
        assertThat(participationRoom.getChatRoom().getName()).isEqualTo("chatroomA");
    }

    @Test
    void certification_메서드는_secretCode를_파라미터로_받아_채팅방의_secretCode_와_맞으면_submitKey_값이_true_인_객체를_반환한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .secretCode("asde#$%")
                .maxChatRoomSize(100)
                .participationNum(0)
                .member(member)
                .build();

        ParticipationRoom participationRoom = ParticipationRoom.create(member, chatroom);

        //when
        participationRoom = participationRoom.certificate("asde#$%");

        //then
        assertThat(participationRoom.getJoined()).isFalse();
        assertThat(participationRoom.getSubmitKey()).isTrue();
        assertThat(participationRoom.getMember()).isEqualTo(member);
        assertThat(participationRoom.getChatRoom()).isEqualTo(chatroom);
    }

    @Test
    void certification_메서드는_secretCode를_파라미터로_받아_채팅방의_secretCode_와_다르면_예외를_발생시킨다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .secretCode("asde#$%")
                .maxChatRoomSize(100)
                .participationNum(0)
                .member(member)
                .build();

        ParticipationRoom participationRoom = ParticipationRoom.create(member, chatroom);

        //when
        //then
        assertThatThrownBy(() -> {
            participationRoom.certificate("qwer");
        }).isInstanceOf(CustomRuntimeException.class);
    }
}
