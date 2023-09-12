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

        .input-form {
            outline: none;
            border: 1px solid lightgray;
            padding: 0.5rem;
            flex-grow: 1;
        }

        .form-border {
            border: 1px solid white;
            border-radius: 4px;
        }

        .room-name {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .room-name .room-name-text {
            font-size: 1.2rem;
        }

        .room-name .lock-icon{
            font-size: 1rem;
            margin-right: 8px;
        }

        .room-name span {
            font-size: .7rem;
        }

    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="row">
        <div class="col-6">
            <h3>채팅방 목록</h3>
        </div>
        <div class="col-6 text-right">
            <a class="btn btn-secondary btn-sm" href="/logout">로그아웃</a>
        </div>
    </div>
    <form @submit.prevent class="form-border mt-4">
        <div class="mb-3">
            <label for="roomName" class="form-label">채팅방 이름</label>
            <input type="text" id="roomName" class="form-control" v-model="roomName" @keyup.enter="createRoom"/>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="password" v-model="roomPassword">
        </div>
        <div class="mb-2 form-check">
            <input type="checkbox" class="form-check-input" id="isPrivateRoom" v-model="isPrivate">
            <label class="form-check-label" for="isPrivateRoom">프라이빗 채팅방</label>
        </div>
        <div class="text-right">
            <button type="submit" class="btn-sm btn btn-primary" @click="createRoom">채팅방 생성</button>
        </div>
    </form>
    <hr/>
    <ul class="list-group mt-4">
        <li class="list-group-item mb-4 list-group-item-action" v-for="room in chatRooms" v-bind:key="room.id">
            <h4 class="room-name"><span class="room-name-text"><span v-if="room.type === 'PRIVATE'" class="lock-icon">&#128274;</span>{{room.name}}</span> <span class="badge bg-primary text-white">{{room.participationNum}} / {{room.maxPeopleAllowNum}}</span></h4>
            <div class="row mt-4">
                <div class="col-6">
                    <button class="btn-sm btn btn-primary" type="button" @click="checkPermission(room.id, room.name, room.type)">입장</button>
                </div>
                <div class="col-6 text-right " v-if="isOwner(room.ownerName)">
                    <button class="btn-sm btn btn-danger" type="button" @click="deleteRoom(room.id)">삭제</button>
                </div>
            </div>

        </li>

    </ul>
</div>
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/bootstrap/4.3.1/dist/js/bootstrap.min.js"></script>
<script>
    const vm = new Vue({
        el: '#app',
        data: {
            roomName : '',
            isPrivate: false,
            roomPassword: null,
            chatRooms: [],
            memberInfo: null
        },
        created() {
            this.findAllRoom();
            this.getMemberInfo();
        },
        computed: {
            isOwner(){
                return (ownerName) => {
                    return this.memberInfo.name === ownerName;
                }
            }
        },
        methods: {
            async deleteRoom(roomId) {
                let result = confirm("채팅방을 삭제 하시겠습니까?");
                if(!result) return;

                await axios.delete('/api/chat/room', {
                    data: {
                        type: "DELETE",
                        requestMemberId: this.memberInfo.id,
                        roomId: roomId
                    }
                }).then(response => {
                    this.findAllRoom();
                });
            },
            async getMemberInfo () {
                await axios.get('/api/member').then(response => {
                    this.memberInfo = response.data.body;
                    localStorage.setItem('memberInfo', JSON.stringify(response.data.body));
                });
            },
            findAllRoom: async function() {
                await axios.get('/api/chat/rooms').then(response => {
                    this.chatRooms = response.data.body;
                });
            },
            createRoom: function() {
                if("" === this.roomName) {
                    alert("Please enter a room title.");
                    return;
                }

                if(this.isPrivate && this.roomPassword.trim() === "") {
                    alert("Please enter the private room key.");
                    return;
                }

                const data = {
                    requestMemberId: this.memberInfo.id,
                    name: this.roomName,
                    type: this.isPrivate ? "PRIVATE": "PUBLIC",
                    secretKey: this.roomPassword,
                    maxPeopleAllowNum: 100
                }


                axios.post('/api/chat/room', data)
                    .then(
                        response => {
                            alert(response.data.body.name+"방 개설에 성공하였습니다.")
                            this.roomName = "";
                            this.isPrivate = false;
                            this.roomPassword = "";
                            this.findAllRoom();
                        }
                    )
                    .catch( () => {
                            alert("채팅방 개설에 실패하였습니다.");
                        }
                    );
            },
            checkPermission: async function(roomId, roomName, type) {
                if(type === "PUBLIC") {
                    this.enterRoom(roomId, roomName);
                    return;
                }

                await axios.post('/api/chat/permission', {
                    requestMemberId: this.memberInfo.id,
                    roomId: roomId,
                }).then(response => {
                    const permissionType = response.data.body.type;

                    if(permissionType === "ALLOW") {
                        this.enterRoom(roomId, roomName);
                    }else if(permissionType === "NEED_PASSWORD") {
                        this.readyEnterRoom(roomId, roomName)
                    }else {
                        alert("채팅방에 입장할 수 없습니다.");
                    }
                });
            },
            readyEnterRoom: async function(roomId, roomName) {
                let key = prompt("비밀번호를 입력해주세요");
                if (key && key.trim() === "") {
                    return;
                }

                await axios.post('/api/chat/key',{
                    memberId: this.memberInfo.id,
                    roomId: roomId,
                    secretCode: key
                }).then(response => {
                    this.enterRoom(roomId, roomName);
                }).catch(() => {
                    alert("비밀번호가 틀렸습니다.");
                });
            },
            enterRoom(roomId, roomName) {
                localStorage.setItem('chatRoom.roomName',roomName);
                localStorage.setItem('chatRoom.roomId',roomId);
                location.href="/chat/room/"+roomId;
            }
        }
    });
</script>
</body>
</html>
