<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-name">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-4">
                <div class="user-select-bot">
                    <select v-model="select_bot" class="form-select" aria-label="Default select example">
                        <option value="-1">In person</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{ bot.title }}
                        </option>
                    </select>
                </div>
            </div>
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-name">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align: center; padding-top: 10vh">
                <button @click="click_match_btn" type="button" class="btn btn-light btn-lg">{{match_btn_info}}</button>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery'

export default {
    setup() {
        const store = useStore();
        let match_btn_info = ref("start matching");
        let bots = ref([]);
        let select_bot = ref("-1");

        const refresh_bots = () => {
            $.ajax({
                url: "https://app2079.acapp.acwing.com.cn/api/user/bot/getlist/",
                // url: "http://127.0.0.1:3000/api/user/bot/getlist/",
                type: "get",
                headers: { // 需要验证身份，即不被放行的页面，均需要headers
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            })
        }

        const click_match_btn = () => {
            if (match_btn_info.value === "start matching") {
                match_btn_info.value = "cancel";
                // Note that socket has an api called send, 
                // which can be used to send a string to the backend
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: select_bot.value
                }));
            } else {
                match_btn_info.value = "start matching";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
            }
        }

        refresh_bots(); // fetch bots from backend database
        return {
            match_btn_info,
            click_match_btn,
            bots,
            select_bot,
        }
    },
    
}
</script>

<style scoped>
div.matchground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    background-color: lightcoral;
}
div.user-photo {
    text-align: center;
    padding-top: 10vh;
}
div.user-photo > img {
    border-radius: 50%;
    width: 20vh;
}
div.user-name {
    text-align: center;
    font-size: 24px;
    font-weight: bold;
    padding-top: 2vh;
}
div.user-select-bot {
    padding-top: 20vh;
}
div.user-select-bot > select {
    width: 60%;
    margin: 0 auto;
}
</style>