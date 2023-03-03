<template>
  <div>
    <div>Bot Name: {{ bot_name }}</div>
    <div>Bot score: {{ bot_score }}</div>
  </div>
  <router-view/>
</template>

<script>
import $ from 'jquery';
import { ref } from 'vue';
export default {
  name: "App",
  setup: () => {
    let bot_name = ref("");
    let bot_score = ref("");

    $.ajax ({
      url: "http://127.0.0.1:3000/pk/getbotinfo/",
      type: "get",
      success: resp => {
        bot_name.value = resp.name;
        bot_score.value = resp.score;
      }
    });

    return {
      bot_name,
      bot_score
    }
  }
}
</script>

<style>
body {
  background-image: url("./assets/background.webp");
  background-size: cover;
}
</style>
