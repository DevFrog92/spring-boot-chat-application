package com.example.chat.repository;

import com.example.chat.domain.ParticipationRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRoomRepository extends JpaRepository<ParticipationRoom, Long> {
    List<ParticipationRoom> findByMemberId(Long memberId);
}
