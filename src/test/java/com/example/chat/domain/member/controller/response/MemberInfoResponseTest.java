package com.example.chat.domain.member.controller.response;

import com.example.chat.domain.member.domain.Member;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberInfoResponseTest {
    @Test
    void from_메서드로_member_와_token_을_파라미터로_받아_MemberInfoResponse_객체를_생성할_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .nickname("member nickname")
                .name("member")
                .build();

        String token = "asdfas-qqwerqwer-vxzcvasdfasdffsdfsdaf";

        //when
        MemberInfoResponse memberInfoResponse = MemberInfoResponse.from(member, token);

        //then
        assertThat(memberInfoResponse.getId()).isEqualTo(1L);
        assertThat(memberInfoResponse.getName()).isEqualTo("member");
        assertThat(memberInfoResponse.getNickname()).isEqualTo("member nickname");
        assertThat(memberInfoResponse.getToken()).isEqualTo("asdfas-qqwerqwer-vxzcvasdfasdffsdfsdaf");
    }
}
