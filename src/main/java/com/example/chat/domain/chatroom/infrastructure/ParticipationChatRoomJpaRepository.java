package com.example.chat.domain.chatroom.infrastructure;

import com.example.chat.domain.chatroom.infrastructure.entity.ChatRoomEntity;
import com.example.chat.domain.chatroom.infrastructure.entity.ParticipationRoomEntity;
import com.example.chat.domain.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipationChatRoomJpaRepository extends JpaRepository<ParticipationRoomEntity, Long> {
    Optional<ParticipationRoomEntity> getByMemberAndRoom(MemberEntity memberEntity, ChatRoomEntity room);

    void deleteByMemberAndRoom(MemberEntity memberEntity, ChatRoomEntity room);

    void deleteAllByRoom(ChatRoomEntity room);

    Optional<ParticipationRoomEntity> getByRoom(ChatRoomEntity room);
}
