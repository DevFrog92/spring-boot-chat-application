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
    <div class="chat-room-title mb-4">
        <h2>{{room.name}}</h2>
    </div>
    <ul class="list-group message-list">
        <li v-for="message in messages" class="message-item-wrapper" :class="isOwner(message.sender)">
            <div class="message-item" >
                <p class="message-sender">
                    {{message.sender}}
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
    var sock = new SockJS("/ws-stomp");
    var ws = Stomp.over(sock);
    var reconnect = 0;

    // vue.js
    var vm = new Vue({
        el: '#app',
        data: {
            roomId: '',
            roomName: '',
            room: {},
            sender: '',
            message: '',
            messages: []
        },
        created() {
            this.roomId = localStorage.getItem('wschat.roomId');
            this.roomName = localStorage.getItem('wschat.roomName');
            var _this = this;

            axios.get('/chat/user')
                .then(response => {
                   _this.token = response.data.token;
                   ws.connect({"token": _this.token}, function(frame) {
                       ws.subscribe("/sub/chat/room/"+_this.roomId, function(message) {
                           var recv = JSON.parse(message.body);
                           _this.recvMessage(recv);
                       });
                       _this.sendMessage('JOIN');
                   }, function (error) {
                       alert("서버 연결에 실패 하였습니다. 다시 접속해 주세요.");
                       location.href = "/chat/room";
                   });
                });
        },
        computed: {
          isOwner() {
              return (name) => {
                  console.log(name, this.sender);
                  return name === this.sender ? "owner" : "";
              }
          }
        },
        methods: {
            sendMessage: function(type) {
                ws.send("/pub/chat/message", {"token": this.token},
                    JSON.stringify({type: type, roomId:this.roomId, message:this.message}));
                this.message = '';
            },
            recvMessage: function(recv) {
                this.messages.push({"type":recv.type,"sender":recv.sender,"message":recv.message});
            }
        }
    });
</script>
</body>
</html>
