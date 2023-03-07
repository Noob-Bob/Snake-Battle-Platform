<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-body-tertiary bg-dark">
        <div class="container">
          <!-- <a class="navbar-brand" href="/">King Of Bots</a> -->
          <router-link class="navbar-brand" :to="{name: 'home'}">King of Bots</router-link>
          <!-- <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button> -->
          <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
              <li class="nav-item">
                <!-- <a class="nav-link" aria-current="page" href="/pk/">Battle</a> -->
                <router-link class="nav-link" active-class="active" aria-current="page" :to="{name: 'pk_index'}">Battle</router-link>
              </li>
              <li class="nav-item">
                <!-- <a class="nav-link" href="/record/">Battle Records</a> -->
                <router-link class="nav-link" active-class="active" :to="{name: 'record_index'}">Battle Record</router-link>
              </li>
              <li class="nav-item">
                <!-- <a class="nav-link" href="/ranklist/">Ranking</a> -->
                <router-link class="nav-link" active-class="active" :to="{name: 'ranklist_index'}">Ranking</router-link>
              </li>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                      Zhangxc
                    </a>
                    <ul class="dropdown-menu">
                      <li>
                        <!-- <a class="dropdown-item" href="/user/bot/">My Bot</a> -->
                        <router-link class="dropdown-item" active-class="active" :to="{name: 'user_bot_index'}">My Bot</router-link>
                      </li>
                      <li><a class="dropdown-item" href="#">Log Out</a></li>
                    </ul>
                  </li>
              </ul>
          </div>
        </div>
      </nav>
</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'

export default {
    setup() {
        const route = useRoute();
        let route_name = computed(() => route.name)
        return {
            route_name
        }
    }
}
</script>

<style scoped>
</style>