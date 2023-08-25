package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.chatroom.domain.BlackList;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.chatroom.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    Optional<BlackList> findByRoomAndMember(Room room, Member member);
}
