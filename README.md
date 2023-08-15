# Spring Boot chat application

Spring Boot와 WebSocket을 이용한 기본적인 실시간 채팅 기능을 구현한다.

1차: Thymeleaf를 View로 실시간 통신이 가능한 채팅방을 구현
2차: 채팅방에 사용자 식별 기능을 추가
3차: Open API 형식으로 구현 뒤, Thymeleaf를 SPA(ex Vue.js)로 재구현

### 메인 기술 스택

- Java 11
- Spring Boot 2.x.x
- Spring WebSocket
- JPA
- MySQL
- Redis(TBC)
- SpringSecurity(TBD)

### 주요 기능

- 사용자 기능
    - 회원가입한 유저만 채팅 관련 기능을 사용할 수 있다.
    - 회원가입한 사용자는 닉네임을 정할 수 있다.
        - 닉네임은 채팅방에서 사용된다.
- 실시간 채팅 기능
    - 참여자 알림기능
    - 채팅 작성 및 전송 기능
- 채팅방 기능
    - 채팅방 참여 기능
        - 회원가입 한 유저는 채팅방에 참여할 수 있다.
            - 프라이빗 채팅방의 경우 식별 코드가 반드시 입력되어야 한다.(TBD)
    - 채팅방 조회 기능
        - 채팅방은 이름 또는 관리자의 이름으로 조회할 수 있다.(TBD)
        - 각 채팅방은 참여인원을 보여준다.
    - 채팅방 생성 기능
        - 채팅방 생성시 비밀번호를 통해서 프라이빗 채팅방을 개설할 수 있다.(TBD)
        - 채팅방에 참여할 수 있는 식별 코드를 생성한다.(TBD)
        - 회원가입한 유저만 채팅 방을 생성할 수 있다.
        - 회원 등급에 따라서 채팅방의 인원을 제한할 수 있다.(TBD)
    - 채팅방 삭제 기능
        - 소유주는 방에 인원이 남아 있는 경우에는 삭제할 수 있다.
    - 방에서 강퇴 기능

- 소유주는 권한을 넘길 수 있을까?
    - 인원이 있는데, 소유주가 나가는 경우에는 어떻게 할까?
    - 남아 있는 사람들이 있어도 삭제

- 관리자에게 문의 기능(TBD)
    - 관리자에게 문의할 수 있다.(TBD)

### ERD(WIP)

![Screenshot 2023-08-10 at 4 26 54 PM](https://github.com/DevFrog92/spring-boot-chat-application/assets/82052272/321d5b73-e569-4cec-9442-c64e8bdcdc9a)


### Reference
- https://www.daddyprogrammer.org/post/4731/spring-websocket-chatting-server-redis-pub-sub/
