package com.example.chat.controller;

import com.example.chat.domain.Member;
import com.example.chat.domain.Room;
import com.example.chat.dto.ChatDeleteDto;
import com.example.chat.dto.ChatKeyDto;
import com.example.chat.dto.ResponseDto;
import com.example.chat.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {
    private final MemberService memberService;
    private final RoomService roomService;
    private final ParticipationRoomService participationRoomService;


    @GetMapping("/room")
    public String room() {
        return "/chat/room";
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<Room> rooms(Authentication auth) {
        return roomService.findAllRooms(auth.getName());
    }

    @PostMapping("/room")
    @ResponseBody
    public Room createRoom(@RequestParam String roomName,
                           @RequestParam Long memberId) {
        Member member = memberService.findMemberById(memberId);
        return roomService.createRoom(member, roomName);
    }

    @PostMapping("/key")
    public ResponseEntity<?> submitSecretKey(@RequestBody ChatKeyDto dto) {
        // 이미 인증된 유저인지 확인한다.

        if (roomService.checkSubmitSecretKey(dto)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(
                HttpStatus.BAD_REQUEST
        );
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model,
                             @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomDetail";
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public Room roomInfo(@PathVariable Long roomId) {
        return roomService.getRoomInfo(roomId);
    }

    @DeleteMapping("/room")
    public void deleteRoom(@RequestBody ChatDeleteDto dto) {
        roomService.deleteRoom(dto);
    }

    @PostMapping("/room/credential")
    public ResponseEntity<?> getMemberCredentialOfRoom(@RequestBody ChatKeyDto dto) {
        Member member = memberService.findMemberById(dto.getMemberId());
        Room room = roomService.findById(dto.getRoomId());
        boolean permitMember = participationRoomService.isPermitMember(member, room);
        return new ResponseEntity<>(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "조회 완료",
                        permitMember
                ),
                HttpStatus.OK
        );
    }

}
