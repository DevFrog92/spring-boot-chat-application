package com.example.chat.domain.member.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MemberTest {

    @Test
    void Member_객체는_MemberCreate_객체로_생성할_수_있다() {
        //given
        MemberCreate memberCreate = MemberCreate.builder()
                .name("member")
                .nickName("member nickname")
                .build();

        //when
        Member member = Member.from(memberCreate);

        //then
        assertThat(member.getName()).isEqualTo("member");
        assertThat(member.getNickname()).isEqualTo("member nickname");
    }

    @Test
    void Member_객체는_MemberUpdate_객체로_닉네임을_변경_할_수_있다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .nickname("member nickname")
                .name("member")
                .build();

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickname("edit member nickname")
                .build();

        //when
        Member updatedMember = member.update(memberUpdate);

        //then
        assertThat(updatedMember.getId()).isEqualTo(1L);
        assertThat(updatedMember.getName()).isEqualTo("member");
        assertThat(updatedMember.getNickname()).isEqualTo("edit member nickname");
    }

}
