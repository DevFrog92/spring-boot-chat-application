# Spring Boot chat application

Redis와 Stomp를 이용한 기본적인 실시간 채팅 애플리케이션

### 메인 기술 스택

- Java 11
- Spring Boot 2.x.x
- WebSocket/Stomp
- JPA
- MySQL
- H2
- Redis Pub/Sub
- Spring Security
- Vue
- Freemarker

### 주요 기능

- 사용자 기능
    - 회원가입한 사용자만 채팅 관련 기능을 사용할 수 있다.
    - 회원가입한 사용자는 닉네임을 정할 수 있다.
        - 닉네임은 채팅방에서 사용된다.
    - 사용자는 로그라웃할 수 있다. 
- 실시간 채팅 기능
    - 채팅 작성 및 전송 기능
- 채팅방 기능
    - 전체 채팅방 조회 기능
        - 메인 페이지에서 생성되어 있는 모든 채팅방을 볼 수 있다.
        - 만약 사용자가 특정 채팅방에 블랙리스트로 등록되어 있다면, 해당 채팅방은 제외되고 보여진다.
    - 채팅방 참여 기능
        - 회원가입 한 유저는 채팅방에 참여할 수 있다.
            - 프라이빗 채팅방의 경우 식별 코드가 반드시 입력되어야 한다.
        - 채팅방에 참여했다는 알림 메시지가 전송된다.
    - 채팅방 나가기 기능
        - 참여자는 채팅방을 나갈 수 있다.
        - 참여자가 나가면 채팅방에는 유저가 나갔다는 알림 메시지가 전송된다.. 
    - 채팅방 생성 기능
        - 채팅방 생성시 비밀번호를 통해서 프라이빗 채팅방을 개설할 수 있다.
        - 채팅방에 참여할 수 있는 식별 코드를 생성한다.
        - 회원가입한 유저만 채팅 방을 생성할 수 있다.
    - 채팅방 삭제 기능
        - 채팅방의 관리인은 채팅방을 삭제할 수 있다.
            - 만약 채팅방에 참여인원이 남아있으면 모든 인원을 내보내고, 채팅방은 삭제된다.
    - 방에서 강퇴 기능
        - 채팅방 관리자는 참여자를 강퇴시킬 수 있다.
        - 강퇴 당한 사용자는 블랙리스트에 추가된다.

### ERD

![Screenshot 2023-08-26 at 12 00 09 AM](https://github.com/DevFrog92/spring-boot-chat-application/assets/82052272/7ff83cda-5614-4d76-9a09-e23ef44e9133)

### ScreenShot

- room
  
  ![Screenshot 2023-08-25 at 11 31 01 PM](https://github.com/DevFrog92/spring-boot-chat-application/assets/82052272/26ab5051-15a9-4808-95d8-8b0b0d63aadf)


### Reference
- https://www.daddyprogrammer.org/post/4731/spring-websocket-chatting-server-redis-pub-sub/
