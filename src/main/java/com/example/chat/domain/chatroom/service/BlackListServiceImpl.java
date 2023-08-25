package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.BlackList;
import com.example.chat.domain.chatroom.domain.BlackListRepository;
import com.example.chat.domain.chatroom.dto.chatroom.RoomDto;
import com.example.chat.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService{
    private final BlackListRepository blackListRepository;

    @Override
    @Transactional
    public void addBlackList(RoomDto room, MemberDto banMember) {
        BlackList blackList = BlackList.builder()
                .room(room.getEntity())
                .member(banMember.getEntity())
                .build();

        blackListRepository.save(blackList);
    }

    @Override
    public boolean isMemberInBlackList(MemberDto member, RoomDto room) {
        return blackListRepository
                .findByRoomAndMember(room.getEntity(), member.getEntity())
                .isPresent();
    }
}
