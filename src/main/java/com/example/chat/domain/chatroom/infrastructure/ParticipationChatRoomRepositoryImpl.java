package com.example.chat.domain.chatroom.infrastructure;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ParticipationRoom;
import com.example.chat.domain.chatroom.infrastructure.entity.ChatRoomEntity;
import com.example.chat.domain.chatroom.infrastructure.entity.ParticipationRoomEntity;
import com.example.chat.domain.chatroom.service.port.ParticipationChatRoomRepository;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.member.infrastructure.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ParticipationChatRoomRepositoryImpl implements ParticipationChatRoomRepository {
    private final ParticipationChatRoomJpaRepository participationChatRoomJpaRepository;
    @Override
    public Optional<ParticipationRoom> getByMemberAndRoom(Member member, ChatRoom chatRoom) {
        return participationChatRoomJpaRepository
                .getByMemberAndRoom(MemberEntity.fromModel(member), ChatRoomEntity.fromModel(chatRoom))
                .stream()
                .map(ParticipationRoomEntity::toModel)
                .findAny();
    }

    @Override
    public void deleteByMemberAndRoom(Member member, ChatRoom chatRoom) {
        participationChatRoomJpaRepository.deleteByMemberAndRoom(
                MemberEntity.fromModel(member),
                ChatRoomEntity.fromModel(chatRoom)
        );
    }

    @Override
    public void deleteAllByRoom(ChatRoom chatRoom) {
        participationChatRoomJpaRepository.deleteAllByRoom(ChatRoomEntity.fromModel(chatRoom));
    }

    @Override
    public ParticipationRoom save(ParticipationRoom participationRoom) {
        return participationChatRoomJpaRepository.save(ParticipationRoomEntity.from(participationRoom)).toModel();
    }
}
