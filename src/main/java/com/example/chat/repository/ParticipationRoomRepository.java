package com.example.chat.repository;

import com.example.chat.domain.Member;
import com.example.chat.domain.ParticipationRoom;
import com.example.chat.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipationRoomRepository extends JpaRepository<ParticipationRoom, Long> {
    Optional<ParticipationRoom> findByMemberAndRoom(Member member, Room room);
    void deleteByRoomAndMember(Room room, Member member);
}
