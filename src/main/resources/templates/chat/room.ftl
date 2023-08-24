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
        <li class="list-group-item list-group-item-action" v-for="item in chatRooms" v-bind:key="item.id">
            {{item.roomName}}
            <h6>{{item.roomName}} <span class="badge badge-info badge-pill">{{item.participationNum}}</span></h6>
            <div @click="checkPermission(item.id, item.member, item.roomName)">입장</div>
            <div v-if="isOwner(item.member.id)" @click="deleteRoom(item.id, item.member)">삭제</div>
        </li>

    </ul>
</div>
<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/bootstrap/4.3.1/dist/js/bootstrap.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    const vm = new Vue({
        el: '#app',
        data: {
            roomName : '',
            chatRooms: [],
            memberInfo: null
        },
        created() {
            this.findAllRoom();
            this.getMemberInfo();
        },
        computed: {
            isOwner(){
                return (roomOwnerId) => {
                    return this.memberInfo.id === roomOwnerId;
                }
            }
        },
        methods: {
            async deleteRoom(roomId, member) {
                let result = confirm("채팅방을 삭제 하시겠습니까?");
                if(!result) return;

                await axios.delete('/chat/room', {data: {
                        type: "DELETE",
                        memberId: member.id,
                        roomId: roomId
                    }
                }).then(response => {
                    this.findAllRoom();
                });
            },
            async getMemberInfo () {
                await axios.get('/member').then(response => {
                    this.memberInfo = response.data;
                    localStorage.setItem('memberInfo', JSON.stringify(response.data));
                });
            },
            findAllRoom: function() {
                axios.get('/chat/rooms').then(response => { this.chatRooms = response.data; });
            },
            createRoom: function() {
                if("" === this.roomName) {
                    alert("Please enter a room title.");
                    return;
                } else {
                    var params = new URLSearchParams();
                    params.append("roomName", this.roomName);
                    params.append("memberId", this.memberInfo.id);
                    axios.post('/chat/room', params)
                        .then(
                            response => {
                                alert(response.data.roomName+"방 개설에 성공하였습니다.")
                                this.roomName = '';
                                this.findAllRoom();
                            }
                        )
                        .catch( () => {
                                alert("채팅방 개설에 실패하였습니다.");
                            }
                        );
                }
            },
            checkPermission: async function(roomId,owner, roomName) {
                await axios.post('/chat/room/credential', {
                    memberId: this.memberInfo.id,
                    roomId: roomId,
                    password: ""
                }).then(response => {
                    const data = response.data;
                    const isPermit = data.body;
                    if(isPermit) {
                        this.enterRoom(roomId, roomName);
                    }else {
                        this.readyEnterRoom(roomId,owner, roomName)
                    }
                });
            },
            readyEnterRoom: async function(roomId, owner, roomName) {
                let key = "";
                if (this.memberInfo.id !== owner.id){
                    key = prompt("비밀번호를 입력해주세요");

                    if (key.trim() === "") {
                        return;
                    }
                }

                await axios.post('/chat/key',{
                    memberId: this.memberInfo.id,
                    roomId: roomId,
                    password: key
                }).then(response => {
                    this.enterRoom(roomId, roomName);
                }).catch(() => {
                    alert("비밀번호가 틀렸습니다.");
                });
            },
            enterRoom(roomId, roomName) {
                localStorage.setItem('chatRoom.roomName',roomName);
                localStorage.setItem('chatRoom.roomId',roomId);
                location.href="/chat/room/enter/"+roomId;
            }
        }
    });
</script>
</body>
</html>
