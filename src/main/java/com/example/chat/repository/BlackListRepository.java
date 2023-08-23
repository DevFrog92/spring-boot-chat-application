package com.example.chat.repository;

import com.example.chat.domain.BlackList;
import com.example.chat.domain.Member;
import com.example.chat.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    Optional<BlackList> findByRoomAndMember(Room room, Member member);
}
