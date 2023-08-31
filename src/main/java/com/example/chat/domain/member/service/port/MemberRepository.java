package com.example.chat.domain.member.service.port;

import com.example.chat.domain.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    Member save(Member member);

}