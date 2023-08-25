package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.chatroom.domain.ParticipationRoom;
import com.example.chat.domain.chatroom.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipationRoomRepository extends JpaRepository<ParticipationRoom, Long> {
    Optional<ParticipationRoom> findByMemberAndRoom(Member member, Room room);

    void deleteByRoomAndMember(Room room, Member member);

    void deleteAllByRoom(Room room);
}
