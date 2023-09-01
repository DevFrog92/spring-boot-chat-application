package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ParticipationRoom;
import com.example.chat.domain.chatroom.service.port.ParticipationChatRoomRepository;
import com.example.chat.domain.chatroom.service.port.ParticipationRoomService;
import com.example.chat.domain.common.domain.Exception.CustomNoSuchElementException;
import com.example.chat.domain.common.domain.Exception.CustomRuntimeException;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.mock.FakeParticipationChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.chat.domain.chatroom.domain.ChatRoomType.PRIVATE;
import static com.example.chat.domain.chatroom.domain.ChatRoomType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParticipationRoomServiceImplTest {
    private ParticipationChatRoomRepository participationChatRoomRepository;
    private ParticipationRoomService participationRoomService;

    @BeforeEach
    void init() {
        this.participationChatRoomRepository = new FakeParticipationChatRoomRepository();
        this.participationRoomService = ParticipationRoomServiceImpl.builder()
                .participationRoomRepository(this.participationChatRoomRepository)
                .build();
    }

    @Test
    void create_는_Member_와_ChatRoom_객체를_받아_ParticipationRoom_객체를_생성후_저장한() {
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
                .participationNum(1)
                .member(member)
                .build();

        //when
        participationRoomService.create(member, chatroom);
        ParticipationRoom participationRoom = participationChatRoomRepository
                .getByMemberAndRoom(member, chatroom).orElseThrow();

        //then

        assertThat(participationRoom).isNotNull();
        assertThat(participationRoom.getChatRoom().getId()).isEqualTo(chatroom.getId());
        assertThat(participationRoom.getMember().getId()).isEqualTo(member.getId());
        assertThat(participationRoom.getJoined()).isFalse();

        // public 의 경우에는 true
        assertThat(participationRoom.getSubmitKey()).isTrue();
    }

    @Test
    void getByMemberAndRoom_는_Member_와_ChatRoom_객체를_받아_ParticipationRoom_객체를_반환한다() {
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
                .participationNum(1)
                .member(member)
                .build();

        //when
        participationRoomService.create(member, chatroom);
        ParticipationRoom findResult = participationRoomService.getByMemberAndRoom(member, chatroom);

        //then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getChatRoom().getId()).isEqualTo(chatroom.getId());
        assertThat(findResult.getMember().getId()).isEqualTo(member.getId());
        assertThat(findResult.getJoined()).isFalse();

        // public 의 경우에는 true
        assertThat(findResult.getSubmitKey()).isTrue();
    }

    @Test
    void getByMemberAndRoom_는_ParticipationRoom_객체가_없으면_null_을_반환한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .member(member)
                .type(PUBLIC)
                .build();

        //when
        ParticipationRoom findResult = participationRoomService.getByMemberAndRoom(member, chatroom);

        //then
        assertThat(findResult).isNull();
    }

    @Test
    void join_은_Member_와_ChatRoom_객체를_받아_Joined_가_true_인_ParticipationRoom_객체를_업데이트_한다() {
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

        //when
        participationRoomService.create(member, chatroom);
        participationRoomService.join(member, chatroom);
        ParticipationRoom findResult = participationRoomService.getByMemberAndRoom(member, chatroom);

        //then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getChatRoom().getId()).isEqualTo(chatroom.getId());
        assertThat(findResult.getChatRoom().getParticipationNum()).isEqualTo(1);
        assertThat(findResult.getMember().getId()).isEqualTo(member.getId());
        assertThat(findResult.getJoined()).isTrue();

        // public 의 경우에는 true
        assertThat(findResult.getSubmitKey()).isTrue();
    }

    @Test
    void join_은_ParticipationRoom_이_없는데_호출하면_예외를_발생한() {
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
                .participationNum(0)
                .member(member)
                .build();

        //when

        //then
        assertThatThrownBy(() -> {
            participationRoomService.join(member, chatroom);
        }).isInstanceOf(CustomNoSuchElementException.class);
    }

    @Test
    void submitSecretKey_은_Member_와_ChatRoom_객체를_받아_채팅방_비밀코드가_맞으면_submitKey가_true_인_ParticipationRoom_객체를_생성한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PRIVATE)
                .secretCode("asde#$%")
                .maxChatRoomSize(100)
                .participationNum(0)
                .member(member)
                .build();

        //when
        participationRoomService.submitSecretKey(member, chatroom, "asde#$%");
        ParticipationRoom findResult = participationRoomService.getByMemberAndRoom(member, chatroom);

        //then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getChatRoom().getId()).isEqualTo(chatroom.getId());
        assertThat(findResult.getMember().getId()).isEqualTo(member.getId());
        assertThat(findResult.getJoined()).isFalse();
        assertThat(findResult.getSubmitKey()).isTrue();
    }

    @Test
    void submitSecretKey_은_Member_와_ChatRoom_객체를_받아_채팅방_비밀코드가_틀리면_예외를_발생한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PRIVATE)
                .secretCode("asde#$%")
                .maxChatRoomSize(100)
                .participationNum(0)
                .member(member)
                .build();

        //when
        //then
        assertThatThrownBy(() -> {
            participationRoomService.submitSecretKey(member, chatroom, "asde#$%123123123");
        }).isInstanceOf(CustomRuntimeException.class);
    }

    @Test
    void isCertifiedMember_은_participationRoom_을_submitKey의_값을_반환한다() {
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
                .participationNum(0)
                .member(member)
                .build();

        //when
        participationRoomService.create(member, chatroom);
        boolean result = participationRoomService.isCertifiedMember(member, chatroom);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void isCertifiedMember_은_participationRoom_이_없으면_false_를_반환한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PRIVATE)
                .secretCode("asde#$%")
                .maxChatRoomSize(100)
                .participationNum(0)
                .member(member)
                .build();

        //when
        boolean result = participationRoomService.isCertifiedMember(member, chatroom);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void alreadyJoined_은_participationRoom_을_joined의_값을_반환한다() {
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
                .participationNum(0)
                .member(member)
                .build();

        //when
        participationRoomService.create(member, chatroom);
        participationRoomService.join(member, chatroom);
        boolean result = participationRoomService.alreadyJoined(member, chatroom);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void alreadyJoined_은_participationRoom_이_없으면_false_를_반환한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PRIVATE)
                .secretCode("asde#$%")
                .maxChatRoomSize(100)
                .participationNum(0)
                .member(member)
                .build();

        //when
        boolean result = participationRoomService.alreadyJoined(member, chatroom);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void deleteAllByRoom_은_모든_participationRoom_을_삭제한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PRIVATE)
                .secretCode("asde#$%")
                .maxChatRoomSize(100)
                .participationNum(0)
                .member(member)
                .build();

        //when
        participationRoomService.create(member, chatroom);
        participationRoomService.deleteAllByRoom(chatroom);
        ParticipationRoom participationRoom = participationRoomService.getByMemberAndRoom(member, chatroom);

        //then
        assertThat(participationRoom).isNull();
    }

    @Test
    void deleteByMemberAndRoom_은_특ㅡ_participationRoom_을_삭제한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoom chatroom = ChatRoom.builder()
                .id(1L)
                .name("chatroomA")
                .type(PRIVATE)
                .secretCode("asde#$%")
                .maxChatRoomSize(100)
                .participationNum(0)
                .member(member)
                .build();

        //when
        participationRoomService.create(member, chatroom);
        participationRoomService.deleteByMemberAndRoom(member, chatroom);
        ParticipationRoom participationRoom = participationRoomService.getByMemberAndRoom(member, chatroom);

        //then
        assertThat(participationRoom).isNull();
    }
}
