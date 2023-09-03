package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.BlackList;
import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.service.port.BlackListRepository;
import com.example.chat.domain.chatroom.service.port.BlackListService;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.mock.FakeBlackListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.example.chat.domain.chatroom.domain.ChatRoomType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;

class BlackListServiceImplTest {

    private BlackListRepository blackListRepository;
    private BlackListService blackListService;

    @BeforeEach
    void init() {
        this.blackListRepository = new FakeBlackListRepository();
        this.blackListService = BlackListServiceImpl.builder()
                .blackListRepository(this.blackListRepository)
                .build();
    }

    @Test
    void create_는_blackList_를_생성한다() {
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

        blackListService.create(chatroom, member);

        //when
        BlackList blackList = blackListRepository.findByRoomAndMember(chatroom, member).orElseThrow();

        //then
        assertThat(blackList.getRoom().getId()).isEqualTo(chatroom.getId());
        assertThat(blackList.getMember().getId()).isEqualTo(member.getId());
        assertThat(blackList.getRegistrationAt()).isEqualTo(LocalDateTime.MIN);
    }

    @Test
    void isMemberInBlackList_는_blackList_에_Member_가_있는지_확인한다() {
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

        blackListService.create(chatroom, member);

        //when
        boolean result = blackListService.isMemberInBlackList(member, chatroom);

        //then
        assertThat(result).isTrue();
    }
}
