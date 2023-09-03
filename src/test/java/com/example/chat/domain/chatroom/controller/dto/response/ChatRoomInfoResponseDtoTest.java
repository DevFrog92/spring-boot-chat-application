package com.example.chat.domain.chatroom.controller.dto.response;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.member.domain.Member;
import org.junit.jupiter.api.Test;

import static com.example.chat.domain.chatroom.domain.ChatRoomType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;

class ChatRoomInfoResponseDtoTest {
    @Test
    void from_메서드로_ChatRoom_을_파라미터로_받아_ChatRoomInfoResponse_객체를_생성할_수_있다() {
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
                .participationNum(20)
                .build();

        //when
        ChatRoomInfoResponseDto result = ChatRoomInfoResponseDto.from(chatroom);


        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("chatroomA");
        assertThat(result.getOwnerName()).isEqualTo("memberA");
        assertThat(result.getType()).isEqualTo(PUBLIC);
        assertThat(result.getMaxPeopleAllowNum()).isEqualTo(100);
        assertThat(result.getParticipationNum()).isEqualTo(20);
    }
}
