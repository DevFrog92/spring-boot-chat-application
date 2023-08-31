package com.example.chat.domain.chatroom.infrastructure;

import com.example.chat.domain.chatroom.infrastructure.entity.ChatRoomEntity;
import com.example.chat.domain.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {
    @Query("SELECT c FROM ChatRoomEntity c " +
            "WHERE c.id NOT IN (SELECT b.room.id FROM BlackListEntity b WHERE b.member = :member)")
    List<ChatRoomEntity> findRoomsNotInBlacklistForMemberEntity(@Param("member") MemberEntity member);
}
