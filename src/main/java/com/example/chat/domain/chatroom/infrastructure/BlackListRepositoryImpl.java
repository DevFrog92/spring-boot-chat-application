package com.example.chat.domain.chatroom.infrastructure;

import com.example.chat.domain.chatroom.domain.BlackList;
import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.infrastructure.entity.BlackListEntity;
import com.example.chat.domain.chatroom.infrastructure.entity.ChatRoomEntity;
import com.example.chat.domain.chatroom.service.port.BlackListRepository;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.member.infrastructure.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlackListRepositoryImpl implements BlackListRepository {
    private final BlackListJpaRepository blackListJpaRepository;

    @Override
    public BlackList save(BlackList blackList) {
        return blackListJpaRepository.save(BlackListEntity.fromModel(blackList)).toModel();
    }

    @Override
    public Optional<BlackList> findByRoomAndMember(ChatRoom room, Member member) {
        return blackListJpaRepository
                .findByRoomAndMember(ChatRoomEntity.fromModel(room), MemberEntity.fromModel(member))
                .map(BlackListEntity::toModel);
    }
}
