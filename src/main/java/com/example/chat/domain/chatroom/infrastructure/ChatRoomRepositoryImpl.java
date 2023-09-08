package com.example.chat.domain.chatroom.infrastructure;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.infrastructure.entity.ChatRoomEntity;
import com.example.chat.domain.chatroom.service.port.ChatRoomRepository;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.member.infrastructure.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    @Override
    public Optional<ChatRoom> getById(long id) {
        return chatRoomJpaRepository.findById(id).map(ChatRoomEntity::toModel);
    }

    @Override
    public List<ChatRoom> findAllRooms(Member member) {
        return chatRoomJpaRepository
                .findRoomsNotInBlacklistForMemberEntity(MemberEntity.fromModel(member))
                .stream().map(ChatRoomEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomJpaRepository.save(ChatRoomEntity.fromModel(chatRoom)).toModel();
    }

    @Override
    public void deleteById(long id) {
        chatRoomJpaRepository.deleteById(id);
    }
}
