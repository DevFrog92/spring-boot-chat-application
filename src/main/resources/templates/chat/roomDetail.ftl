<!doctype html>
<html lang="en">
<head>
    <title>Websocket ChatRoom</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        [v-cloak] {
            display: none;
        }

        body {
            padding-top: 3rem;
            color: #292929;
        }

        .message-list {
            width: 100%;
            height: 70vh;
            border: 1px solid lightgray;
            margin-bottom: 1rem;
            padding: 0.5rem;
            overflow-x: hidden;
            overflow-y: auto;
            border-radius: 4px;
        }

        .message-item-wrapper {
            width: 100%;
            display: flex;
        }

        .owner {
            text-align: right;
            justify-content: flex-end;
        }


        .message-item {
            width: fit-content;
            height: fit-content;
            margin-bottom: 1rem;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            border: 1px solid lightgray;
            border-radius: 4px;
            padding: 0.5rem;
            background-color: rgba(241, 246, 251, 0.7);
        }

        .text-center {
            margin: 0 auto 1rem;
            flex-grow: 0.8;
        }

        .text-center p {
            text-align: center;
        }


        .message-sender {
            font-size: 0.8rem;
            margin-bottom: 0;
            width: 100%;
        }

        .message-content {
            margin-bottom: 0;
            margin-top: 8px;
            word-wrap: break-word;
        }

        .input-form {
            outline: none;
            border: 1px solid lightgray;
            padding: 0.5rem;
            flex-grow: 1;
            border-radius: 4px;
            height: 3rem;
        }

        .btn-border {
            border-radius: 4px;
            border-top-left-radius: 0;
            border-bottom-left-radius: 0;
            padding: 0.5rem;
            width: 64px;
        }
    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="row">
        <div class="col-md-6 chat-room-title mb-4">
            <h2>{{room.name}}</h2> <span class="badge badge-info badge-pill">{{userCount}}</span></h4>
        </div>
        <div class="col-md-6 text-right">
            <a class="btn btn-primary btn-sm" href="/logout">로그아웃</a>
            <p @click="leaveRoom">채팅방 나가기</p>
        </div>
    </div>
    <ul class="list-group message-list">
        <li v-for="message in messages" class="message-item-wrapper" :class="isUser(message.sender)">
<#--            <div v-if="!isNotice(message.sender)" class="icon-text">{{iconText(message.sender)}}</div>-->
            <div class="message-item" :class="isNotice(message.sender)" >
                <p class="message-sender">
                    {{message.sender}} <span v-if="isOwner && !isNotice(message.sender)" @click="banMember(message.sender)">강퇴</span>
                </p>
                <p class="message-content">
                    {{message.message}}
                </p>
            </div>

        </li>
    </ul>
    <div class="input-group">

        <input type="text" class="form-control input-form" v-model="message" @keyup.enter="sendMessage('MESSAGE')">
        <div class="input-group-append">
            <button class="btn btn-primary btn-border" type="button" @click="sendMessage('MESSAGE')">보내기</button>
        </div>
    </div>
    <div></div>
</div>
<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/bootstrap/4.3.1/dist/js/bootstrap.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    // websocket & stomp initialize
    const sock = new SockJS("/ws-stomp");
    const ws = Stomp.over(sock);
    const reconnect = 0;

    // vue.js
    const vm = new Vue({
        el: '#app',
        data: {
            roomId: '',
            roomName: '',
            room: {},
            sender: '',
            message: '',
            messages: [],
            userCount: 0,
            memberInfo: null,
            roomInfo: {},
        },
        async created() {
            this.memberInfo = JSON.parse(localStorage.getItem('memberInfo'));
            this.roomId = localStorage.getItem('chatRoom.roomId');
            this.roomName = localStorage.getItem('chatRoom.roomName');
            const _this = this;

            await axios.get('/chat/room/'+ this.roomId)
                .then(response => {
                    _this.roomInfo = response.data;
                    _this.userCount = response.data.participationNum;
                   ws.connect({"token": _this.memberInfo.token}, function(frame) {
                       console.log("frame", frame);
                       ws.subscribe("/sub/chat/room/"+_this.roomId, function(message) {
                           console.log("subscribe",_this.roomId);
                           const subscribe = JSON.parse(message.body);

                           if(subscribe.type === "DELETE") {
                               alert("채팅방이 방장에의해 삭제되었습니다.");
                               _this.leaveRoom();
                               return;
                           } else if(subscribe.type === "INFO") {
                               _this.userCount = subscribe.participationNum;
                               return;

                           }

                           _this.subscribeMessage(subscribe);
                       });

                       ws.subscribe("/sub/member/"+_this.memberInfo.id, function(message) {
                           const body = JSON.parse(message.body);

                           if(body.type === "BAN") {
                               alert("방장에 의해 강퇴당하셨습니다.");
                           }else if(body.type === "INVALID") {
                               alert("채팅방에 입장할 수 없습니다.");
                           }

                          _this.leaveRoom();
                       });

                       ws.send("/pub/chat/enter", {"token": _this.memberInfo.token},
                           JSON.stringify({type: 'JOIN', roomId: _this.roomId, loginInfo:_this.memberInfo}), function(message) {
                                const body = JSON.parse(message.body);
                                console.log("message", body);
                           });
                   }, function (error) {
                       alert("Connection fail!!", error);
                       location.href = "/chat/room";
                   });
                });
        },
        computed: {
            iconText() {
              return (text) => {
                  return text.charAt(0).toUpperCase();
              }
            },
            isNotice() {
                return (text) => {
                    return text && text.indexOf('Notice') !== -1 ? "text-center" : "";
                }
            },
            isUser() {
                return (name) => {
                    return name === this.memberInfo.name ? "owner" : "";
                }
              },
            isOwner() {
              return this.memberInfo.id === this.roomInfo.member.id;
            }
        },
        methods: {
            banMember: function(username) {
                ws.send("/pub/chat/ban", {"token": this.memberInfo.token},
                JSON.stringify({type: 'BAN', roomId: this.roomId, requestMemberId: this.memberInfo.id, banMemberName: username}))
            },
            leaveRoom: function() {
                ws.send("/pub/chat/leave", {"token": this.memberInfo.token},
                    JSON.stringify({type: 'QUIT', roomId: this.roomId, loginInfo:this.memberInfo}));

                location.href="/chat/room"
            },
            sendMessage: function(type) {
                ws.send("/pub/chat/message", {"token": this.memberInfo.token},
                    JSON.stringify({type: type, roomId: this.roomId, message:this.message}));

                this.message = '';
            },
            subscribeMessage: function(subscribe) {
                this.messages.push(
                    {
                        "type":subscribe.type,
                        "sender":subscribe.sender,
                        "message":subscribe.message
                    }
                );

                const list = document.querySelector(".message-list");
                if(list != null) {
                    list.scrollTo(list.scrollHeight);
                }
            }
        }
    });
</script>
</body>
</html>
