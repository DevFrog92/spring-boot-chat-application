package com.example.chat.domain.chatroom.controller;

import com.example.chat.domain.chatroom.dto.message.ChatNoticeDto;
import com.example.chat.global.web.dto.ResponseDto;
import com.example.chat.domain.chatroom.dto.chatroom.RoomInfoDto;
import com.example.chat.domain.chatroom.dto.chatroom.SubmitSecretKeyDto;
import com.example.chat.domain.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomApiController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    public ResponseEntity<?> rooms(Authentication auth) {
        List<RoomInfoDto> allRooms = chatRoomService.findAllRooms(auth.getName());

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "모든 채팅방 조회를 성공했습니다.",
                        allRooms
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<?> roomInfo(@PathVariable Long roomId) {
        RoomInfoDto roomInfo = chatRoomService.getRoomInfo(roomId);
        return new ResponseEntity<>(
                new ResponseDto<>(
                        "채팅방 정보를 조회했습니다.",
                        roomInfo
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<?> createRoom(@RequestParam Long memberId,
                                        @RequestParam String roomName) {
        RoomInfoDto room = chatRoomService.createRoom(memberId, roomName);

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "모든 채팅방 조회를 성공했습니다.",
                        room
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/key")
    public ResponseEntity<?> submitSecretKey(@RequestBody SubmitSecretKeyDto dto) {
        boolean result = chatRoomService.checkSubmitSecretKey(dto);
        HttpStatus status = result ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        String message = result ? "인증에 성공했습니다." : "비밀번호를 확인해주세요.";
        return new ResponseEntity<>(
                new ResponseDto<>(
                        message,
                        null
                ),
                status);
    }

    @PostMapping("/room/credential")
    public ResponseEntity<?> getMemberCredentialOfChatRoom(@RequestBody SubmitSecretKeyDto dto) {
        boolean isPermitted = chatRoomService.isPermitMemberEnterChatRoom(
                dto.getRoomId(),
                dto.getMemberId());

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "신원 조회 완료",
                        isPermitted
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/room")
    public void deleteRoom(@RequestBody ChatNoticeDto dto) {
        chatRoomService.deleteRoom(dto);
    }
}
