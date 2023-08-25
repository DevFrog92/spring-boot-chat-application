package com.example.chat.domain.chatroom.dto.chatroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitSecretKeyDto {
    private Long memberId;
    private Long roomId;
    private String password;

    @Override
    public String toString() {
        return "ChatKeyDto{" +
                "memberId=" + memberId +
                ", roomId=" + roomId +
                ", password='" + password + '\'' +
                '}';
    }
}
