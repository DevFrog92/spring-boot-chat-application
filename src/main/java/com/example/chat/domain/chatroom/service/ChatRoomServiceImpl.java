package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ChatRoomCreate;
import com.example.chat.domain.chatroom.service.port.ChatRoomRepository;
import com.example.chat.domain.chatroom.service.port.ChatRoomService;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.common.domain.Exception.CustomNoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public ChatRoom create(Member member, ChatRoomCreate chatRoomCreate) {
        ChatRoom chatRoom = ChatRoom.create(member, chatRoomCreate);
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    @Transactional
    public void updateParticipationNum(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }


    @Override
    public ChatRoom getInfo(Long roomId) {
        return chatRoomRepository.getById(roomId)
                .orElseThrow(() ->
                        new CustomNoSuchElementException("id room not exist"));
    }

    @Override
    public List<ChatRoom> findAllRooms(Member member) {
        return chatRoomRepository.findAllRooms(member);
    }

    @Override
    @Transactional
    public void deleteRoom(Member member, ChatRoom chatRoom) {
        if (!Objects.equals(chatRoom.getOwnerId(), member.getId())) {
            // todo exception member not allow delete room
            return;
        }

        chatRoomRepository.deletById(chatRoom.getId());
    }

    @Override
    public ChatRoom getById(Long id) {
        return chatRoomRepository.getById(id)
                .orElseThrow(() -> new CustomNoSuchElementException("Room does not exist"));
    }
}
