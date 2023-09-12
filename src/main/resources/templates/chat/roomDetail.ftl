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
            padding: 1rem;
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
            max-width: 65%;
            word-break: break-word;
            height: fit-content;
            margin-bottom: 2rem;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            border: 1px solid lightgray;
            border-radius: 4px;
            padding: 1rem;
            background-color: rgba(241, 246, 251, 0.7);
        }

        .text-center {
            margin: 0 auto 1rem;
            flex-grow: 0.8;
            background-color: lightblue;
        }

        .text-center p {
            text-align: center;
        }


        .message-sender {
            font-size: 0.9rem;
            margin-bottom: 0;
            width: 100%;
            text-decoration: underline;
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

        .ban-btn {
            margin-left: 0.5rem;
            color: lightslategray;
        }

        .btn-container {
            display: flex;
            justify-content: flex-end;
        }

        .room-name {
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .room-name span {
            font-size: 0.9rem;
        }
    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="row">
        <div class="col-12 chat-room-title mb-4">
            <h2 class="room-name">{{roomName}} <span class="badge bg-primary text-white">{{userCount}}</span></h2>
        </div>
        <div class="col-12 mb-4 text-right btn-container">
            <button class="btn-sm btn btn-secondary mr-3" @click="moveToHome">메인 화면으로</button>
            <button class="btn-sm btn btn-warning" @click="leaveRoom">채팅방 나가기</button>
        </div>
    </div>
    <ul class="list-group message-list">
        <li v-for="message in messages" class="message-item-wrapper" :class="isUser(message.sender)">
            <div class="message-item" :class="isNotice(message.sender)" >
                <p class="message-sender">
                    {{message.sender}} <span v-if="isOwner && !isNotice(message.sender) && !isUser(message.sender)" class="ban-btn" @click="banMember(message.sender)">[강퇴]</span>
                </p>
                <p class="message-content" :class="isNotice(message.sender)">
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

<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/bootstrap/4.3.1/dist/js/bootstrap.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    const sock = new SockJS("/ws-stomp");
    const ws = Stomp.over(sock);
    ws.debug = null;

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

            await axios.get('/api/chat/room/'+ this.roomId)
                .then(response => {
                    _this.roomInfo = response.data.body;
                    _this.userCount = response.data.body.participationNum;

                   ws.connect({"token": _this.memberInfo.token}, function(frame) {
                       console.log(frame)
                       ws.subscribe("/sub/chat/room/"+_this.roomId, function(message) {
                           const subscribe = JSON.parse(message.body);

                           if(subscribe.type === "DELETE") {
                               alert("방장이 채팅방을 삭제했습니다.");
                               _this.moveToHome();
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

                       _this.enterChatRoom(_this.memberInfo.id, _this.roomId);

                   }, function (error) {
                       alert("Connection fail!", error);
                       location.href = "/chat/room";
                   });
                }).catch(() => {
                    this.moveToHome();
                });
        },
        beforeDestroy() {
            this.clearLocalhost();
        },
        computed: {
            iconText() {
              return (text) => {
                  return text.charAt(0).toUpperCase();
              }
            },
            isNotice() {
                return (text) => {
                    return text && text.indexOf('알림') !== -1 ? "text-center" : "";
                }
            },
            isUser() {
                return (name) => {
                    return name === this.memberInfo.name ? "owner" : "";
                }
              },
            isOwner() {
              return this.memberInfo.name === this.roomInfo.ownerName;
            },
        },
        methods: {
            async enterChatRoom(requestMemberId, roomId) {
                await axios.post('/api/chat/room/'+roomId+'/join', {
                    requestMemberId,
                    roomId,
                }).catch((error) => {
                    console.log(error.response);
                    alert(error.response.data.message);
                    this.moveToHome();
                })
            },
            moveToHome() {
                location.href="/chat/room"
            },
            banMember: async function(username) {
                await axios.post('/api/chat/room/'+this.roomId+'/ban/'+username, {
                    requestMemberId: this.memberInfo.id,
                    roomId: this.roomId,
                    banMemberName: username
                }).catch((error) => {
                        console.log(error.response);
                        alert(error.response.data.message);
                    })
            },
            leaveRoom: async function() {
                await axios.post('/api/chat/room/'+this.roomId+'/leave', {
                    requestMemberId: this.memberInfo.id,
                    roomId: this.roomId,
                }).then((response) => {
                    this.moveToHome();
                })
                    .catch((error) => {
                    console.log(error.response);
                    alert(error.response.data.message);
                    this.moveToHome();
                })
            },
            sendMessage: function(type) {
                ws.send("/pub/chat/message", {"token": this.memberInfo.token},
                    JSON.stringify({type: type, roomId: this.roomId, message:this.message, sender: this.memberInfo.name}));

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
            },
            clearLocalhost() {
                localStorage.removeItem("chatRoom.roomId");
                localStorage.removeItem("chatRoom.roomName");
            }
        }
    });
</script>
</body>
</html>
