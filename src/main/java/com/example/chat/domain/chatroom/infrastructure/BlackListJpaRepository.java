package com.example.chat.domain.chatroom.infrastructure;

import com.example.chat.domain.chatroom.infrastructure.entity.BlackListEntity;
import com.example.chat.domain.chatroom.infrastructure.entity.ChatRoomEntity;
import com.example.chat.domain.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListJpaRepository extends JpaRepository<BlackListEntity, Long> {
    Optional<BlackListEntity> findByRoomAndMember(ChatRoomEntity room, MemberEntity memberEntity);
}
