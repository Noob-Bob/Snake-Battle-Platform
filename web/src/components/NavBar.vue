<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-body-tertiary bg-dark">
        <div class="container">
          <router-link class="navbar-brand" :to="{name: 'home'}">King of Bots</router-link>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
              <li class="nav-item">
                <router-link class="nav-link" active-class="active" aria-current="page" :to="{name: 'pk_index'}">Battle</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" active-class="active" :to="{name: 'record_index'}">Battle Record</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" active-class="active" :to="{name: 'ranklist_index'}">Ranking</router-link>
              </li>
            </ul>
            <ul class="navbar-nav" v-if="$store.state.user.is_login">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                      {{ $store.state.user.username }}
                    </a>
                    <ul class="dropdown-menu">
                      <li>
                        <router-link class="dropdown-item" active-class="active" :to="{name: 'user_bot_index'}">My Bot</router-link>
                      </li>
                      <li>
                        <a class="dropdown-item" href="#" @click="logout">Log Out</a>
                      </li>
                    </ul>
                  </li>
            </ul>
            <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
              <li class="nav-item dropdown">
                  <router-link class="nav-link" :to="{name: 'user_account_login'}" role="button">
                    login
                  </router-link>
              </li>
              <li class="nav-item dropdown">
                <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
                    register
                </router-link>
            </li>
            </ul>
              
          </div>
        </div>
      </nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import { useStore } from 'vuex';

export default {
  setup() {
    const store = useStore();
    const route = useRoute();

    /** used to know which page we are currently stay in
     * to adjust the active attributes of navbar items
     **/
    let route_name = computed(() => route.name);

    const logout = () => {
      store.dispatch("logout");
    }

    return {
      route_name,
      logout
    }
  }
}
</script>

<style scoped>
</style>
