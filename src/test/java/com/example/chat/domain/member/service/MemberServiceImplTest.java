package com.example.chat.domain.member.service;

import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.member.service.port.MemberService;
import com.example.chat.domain.mock.FakeMemberRepository;
import com.example.chat.domain.common.domain.Exception.CustomNoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceImplTest {
    private MemberService memberService;

    @BeforeEach
    void init() {
        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();
        this.memberService = MemberServiceImpl.builder()
                .memberRepository(fakeMemberRepository)
                .build();

        Member memberA = Member.builder()
                .id(1L)
                .name("memberA")
                .nickname("nicknameA")
                .build();

        fakeMemberRepository.save(memberA);
    }

    @Test
    void getByName은_name_으로_멤버를_찾을_수_있다() {
        //given
        String name = "memberA";
        //when
        Member member = memberService.getByName(name);
        //then
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getName()).isEqualTo("memberA");
        assertThat(member.getNickname()).isEqualTo("nicknameA");
    }

    @Test
    void getById는_Id_로_멤버를_찾을_수_있다() {
        //given
        Long id = 1L;
        //when
        Member member = memberService.getById(id);
        //then
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getName()).isEqualTo("memberA");
        assertThat(member.getNickname()).isEqualTo("nicknameA");
    }

    @Test
    void getByName은_유효하지_않은_name_으로_멤버를_찾으면_예외가_발생한다() {
        //given
        String name = "Unknown";

        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            memberService.getByName(name);
        }).isInstanceOf(CustomNoSuchElementException.class);
    }

    @Test
    void getById는_유효하지_않은_Id_로_멤버를_찾으면_예외가_발생한다() {
        //given
        Long id = -1L;
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
            memberService.getById(id);
        }).isInstanceOf(CustomNoSuchElementException.class);
    }

}
