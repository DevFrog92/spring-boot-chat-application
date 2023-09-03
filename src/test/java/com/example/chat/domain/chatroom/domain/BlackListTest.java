package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.member.domain.Member;
import org.junit.jupiter.api.Test;

import static com.example.chat.domain.chatroom.domain.ChatRoomType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;

class BlackListTest {
    @Test
    void create_는_Member_와_ChatRoom_객체를_파라미터로_받아_BlackList_객체를_생성할_수_있다() {
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

        BlackList blackList = BlackList.create(member, chatroom);

        //then
        assertThat(blackList.getRoom().getId()).isEqualTo(1L);
        assertThat(blackList.getRoom().getName()).isEqualTo("chatroomA");
        assertThat(blackList.getMember().getId()).isEqualTo(1L);
        assertThat(blackList.getMember().getName()).isEqualTo("memberA");
    }
}
