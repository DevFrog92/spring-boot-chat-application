<!doctype html>
<html lang="en">
<head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <!-- CSS -->
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

        .input-group-prepend {
            border: 1px solid lightgray;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 0.5rem;
            border-radius: 4px;
            border-top-right-radius: 0;
            border-bottom-right-radius: 0;
        }

        .input-form {
            outline: none;
            border: 1px solid lightgray;
            padding: 0.5rem;
            flex-grow: 1;
        }

        .btn-border {
            border-radius: 4px;
            border-top-left-radius: 0;
            border-bottom-left-radius: 0;
            padding: 0.5rem;
        }


    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="row mb-4">
        <div class="col-md-6 ">
            <h3>채팅방 목록</h3>
        </div>
        <div class="col-md-6 text-right">
            <a class="btn btn-primary btn-sm" href="/logout">로그아웃</a>
        </div>
    </div>
    <div class="input-group mb-4">
        <div class="input-group-prepend">
            <label class="input-group-label mb-0">채팅방 제목</label>
        </div>
        <input type="text" class="input-form" v-model="roomName" @keyup.enter="createRoom"/>
        <div class="input-group-append">
            <button class="btn btn-primary btn-border" type="button" @click="createRoom">채팅방 만들기</button>
        </div>
    </div>
    <ul class="list-group">
        <li class="list-group-item list-group-item-action" v-for="item in chatRooms" v-bind:key="item.roomId"
            v-on:click="enterRoom(item.roomId)">
            {{item.name}}
            <h6>{{item.name}} <span class="badge badge-info badge-pill">{{item.userCount}}</span></h6>
        </li>

    </ul>
</div>
<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/bootstrap/4.3.1/dist/js/bootstrap.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script>
    const vm = new Vue({
        el: '#app',
        data: {
            roomName : '',
            chatRooms: []
        },
        created() {
            this.findAllRoom();
        },
        methods: {
            findAllRoom: function() {
                axios.get('/chat/rooms').then(response => { this.chatRooms = response.data; });
            },
            createRoom: function() {
                if("" === this.roomName) {
                    alert("Please enter a room title.");
                    return;
                } else {
                    var params = new URLSearchParams();
                    params.append("name", this.roomName);
                    axios.post('/chat/room', params)
                        .then(
                            response => {
                                alert(response.data.name+"방 개설에 성공하였습니다.")
                                this.roomName = '';
                                this.findAllRoom();
                            }
                        )
                        .catch( response => {
                                alert("채팅방 개설에 실패하였습니다.");
                            }
                        );
                }
            },
            enterRoom: function(roomId, roomName) {
                localStorage.setItem('chatRoom.roomName',roomName);
                localStorage.setItem('chatRoom.roomId',roomId);
                location.href="/chat/room/enter/"+roomId;
            }
        }
    });
</script>
</body>
</html>
