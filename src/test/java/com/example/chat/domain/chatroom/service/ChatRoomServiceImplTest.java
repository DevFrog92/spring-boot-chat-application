package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.dto.ChatRoomCreate;
import com.example.chat.domain.chatroom.service.port.ChatRoomRepository;
import com.example.chat.domain.chatroom.service.port.ChatRoomService;
import com.example.chat.domain.common.domain.Exception.CustomNoSuchElementException;
import com.example.chat.domain.common.domain.Exception.CustomRuntimeException;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.mock.FakeChatRoomRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.chat.domain.chatroom.domain.ChatRoomType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChatRoomServiceImplTest {

    private ChatRoomService chatRoomService;
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void init() {
        this.chatRoomRepository = new FakeChatRoomRepository();
        this.chatRoomService = ChatRoomServiceImpl.builder()
                .chatRoomRepository(chatRoomRepository)
                .build();
    }

    @Test
    void create_로_새로운_채팅방을_생성할_수_있고_Member_와_ChatRoomCreate_를_매개변수로_받는다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatroom = ChatRoomCreate.builder()
                .requestMemberId(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .secretKey("asde#$%")
                .maxPeopleAllowNum(100)
                .build();

        //when
        chatRoomService.create(member, chatroom);
        ChatRoom chatRoom = chatRoomRepository.getById(member.getId()).orElseThrow();

        //then
        assertThat(chatRoom).isNotNull();
        assertThat(chatRoom.getId()).isEqualTo(1L);
        assertThat(chatRoom.getName()).isEqualTo("chatroomA");
        assertThat(chatRoom.getType()).isEqualTo(PUBLIC);
        assertThat(chatRoom.getSecretCode()).isEqualTo("asde#$%");
        assertThat(chatRoom.getMaxChatRoomSize()).isEqualTo(100);
        assertThat(chatRoom.getParticipationNum()).isEqualTo(0);
        assertThat(chatRoom.getMember()).isEqualTo(member);
    }

    @Test
    void updateParticipationNum_로_채팅방_참여인원을_변경할_수_있다_join_으로_증가하는_경우() {
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
        chatRoomService.updateParticipationNum(chatroom.join());
        ChatRoom chatRoom = chatRoomRepository.getById(member.getId()).orElseThrow();

        //then
        assertThat(chatRoom).isNotNull();
        assertThat(chatRoom.getId()).isEqualTo(1L);
        assertThat(chatRoom.getName()).isEqualTo("chatroomA");
        assertThat(chatRoom.getType()).isEqualTo(PUBLIC);
        assertThat(chatRoom.getSecretCode()).isEqualTo("asde#$%");
        assertThat(chatRoom.getMaxChatRoomSize()).isEqualTo(100);
        assertThat(chatRoom.getParticipationNum()).isEqualTo(1);
        assertThat(chatRoom.getMember()).isEqualTo(member);
    }

    @Test
    void updateParticipationNum_로_채팅방_참여인원을_변경할_수_있다_leave_로_감소하는_경우() {
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
        chatRoomService.updateParticipationNum(chatroom.leave());
        ChatRoom chatRoom = chatRoomRepository.getById(member.getId()).orElseThrow();

        //then
        assertThat(chatRoom).isNotNull();
        assertThat(chatRoom.getId()).isEqualTo(1L);
        assertThat(chatRoom.getName()).isEqualTo("chatroomA");
        assertThat(chatRoom.getType()).isEqualTo(PUBLIC);
        assertThat(chatRoom.getSecretCode()).isEqualTo("asde#$%");
        assertThat(chatRoom.getMaxChatRoomSize()).isEqualTo(100);
        assertThat(chatRoom.getParticipationNum()).isEqualTo(0);
        assertThat(chatRoom.getMember()).isEqualTo(member);
    }

    @Test
    void getById_로_유효한_Id_를_받아_채팅방을_찾을_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatRoomCreate = ChatRoomCreate.builder()
                .requestMemberId(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .secretKey("asde#$%")
                .maxPeopleAllowNum(100)
                .build();


        //when
        chatRoomService.create(member, chatRoomCreate);
        ChatRoom chatRoom = chatRoomService.getById(1L);

        //then
        assertThat(chatRoom).isNotNull();
        assertThat(chatRoom.getId()).isEqualTo(1L);
        assertThat(chatRoom.getName()).isEqualTo("chatroomA");
        assertThat(chatRoom.getType()).isEqualTo(PUBLIC);
        assertThat(chatRoom.getSecretCode()).isEqualTo("asde#$%");
        assertThat(chatRoom.getMaxChatRoomSize()).isEqualTo(100);
        assertThat(chatRoom.getParticipationNum()).isEqualTo(0);
        assertThat(chatRoom.getMember()).isEqualTo(member);
    }

    @Test
    void getById_로_유효하지_않는_Id_를_넘기면_예외가_발생한다() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            chatRoomService.getById(1L);
        }).isInstanceOf(CustomNoSuchElementException.class);
    }

    @Test
    void findAllRooms_로_Member_객체가_블랙리스트로_등록되지_않은_모든_채팅방을_조회할_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatRoomCreate = ChatRoomCreate.builder()
                .requestMemberId(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .secretKey("asde#$%")
                .maxPeopleAllowNum(100)
                .build();

        //when
        chatRoomService.create(member, chatRoomCreate);
        List<ChatRoom> allRooms = chatRoomService.findAllRooms(member);

        //then
        assertThat(allRooms.size()).isEqualTo(1);
    }

    @Test
    void deleteRoom_로_채팅방_관리자는_채팅방을_삭제할_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        ChatRoomCreate chatRoomCreate = ChatRoomCreate.builder()
                .requestMemberId(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .secretKey("asde#$%")
                .maxPeopleAllowNum(100)
                .build();

        //when
        ChatRoom chatRoom = chatRoomService.create(member, chatRoomCreate);
        chatRoomService.deleteRoom(member, chatRoom);
        ChatRoom findRoom = chatRoomRepository.getById(chatRoom.getId()).orElseThrow();

        //then
        assertThat(findRoom).isNotNull();
        assertThat(findRoom.getDisabledAt()).isEqualTo(LocalDateTime.MIN);
    }

    @Test
    void deleteRoom_로_채팅방_관리자가_아니면_채팅방을_삭제할_수_있고_예외가_발생한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("memberA nickname")
                .build();

        Member guest = Member.builder()
                .id(2L)
                .name("guest")
                .nickname("guest nickname")
                .build();

        ChatRoomCreate chatRoomCreate = ChatRoomCreate.builder()
                .requestMemberId(1L)
                .name("chatroomA")
                .type(PUBLIC)
                .secretKey("asde#$%")
                .maxPeopleAllowNum(100)
                .build();

        //when
        ChatRoom chatRoom = chatRoomService.create(member, chatRoomCreate);


        //then
        assertThatThrownBy(() -> {
            chatRoomService.deleteRoom(guest, chatRoom);
        }).isInstanceOf(CustomRuntimeException.class);
    }
}
