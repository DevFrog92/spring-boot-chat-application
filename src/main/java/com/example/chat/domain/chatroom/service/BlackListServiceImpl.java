package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.BlackList;
import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.service.port.BlackListRepository;
import com.example.chat.domain.chatroom.service.port.BlackListService;
import com.example.chat.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {
    private final BlackListRepository blackListRepository;

    @Override
    @Transactional
    public void create(ChatRoom room, Member member) {
        BlackList blackList = BlackList.create(member, room);
        blackListRepository.save(blackList);
    }

    @Override
    public boolean isMemberInBlackList(Member member, ChatRoom room) {
        return blackListRepository.findByRoomAndMember(room, member).isPresent();
    }
}
