package com.example.chat.repository;

import com.example.chat.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT c FROM Room c " +
            "WHERE c.id NOT IN (SELECT b.room.id FROM BlackList b WHERE b.member.id = :memberId)")
    List<Room> findRoomsNotInBlacklistForUser(@Param("memberId") Long memberId);
}
