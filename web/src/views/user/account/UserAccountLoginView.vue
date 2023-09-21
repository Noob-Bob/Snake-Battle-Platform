<template>
    <ContentField v-if="!$store.state.user.pulling_info"> <!--when not pulling_info, display the content-->
        Login
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login"> <!--prevent the default behavior of the form-->
                    <div class="mb-3">
                      <label for="username" class="form-label">Username</label>
                      <input v-model="username" type="text" class="form-control" id="username" aria-describedby="usernameHelp">
                      <div id="usernameHelp" class="form-text">Please input your username.</div>
                    </div>
                    <div class="mb-3">
                      <label for="Password" class="form-label">Password</label>
                      <input v-model="password" type="password" class="form-control" id="Password1">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">Submit</button>
                  </form>
            </div>   
        </div>
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import { useStore } from 'vuex'
import { ref } from 'vue'
import router from '../../../router/index'
export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore(); // get the global store 
        let username = ref('');
        let password = ref('');
        let error_message = ref('');
        // if jwt-token exists, update user token
        // then try to fetch info of the user from backend
        // if success, jump to the home page
        const jwt_token = localStorage.getItem("jwt_token");
        if (jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo", { // use dispatch to call methods defined in actions
                success() {
                    router.push({name: "home"});
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        } else {
            store.commit("updatePullingInfo", false); // use commit for methods in mutations; dispatch for those actions
        }

        // call this function after clicking the submit button
        const login = () => {
            error_message.value = "";
            store.dispatch("login", {
                username: username.value, // use .value to get the value of a ref variable  
                password: password.value,
                success() {
                    store.dispatch("getinfo", {
                        success() {
                            router.push({name: 'home'}); // jump to the homepage
                        }
                    })
                },
                error() {
                    error_message.value = "Incorrent username or password";
                }
            })
        }
        return {
            username, 
            password,
            error_message,
            login,
        }
    }

}
</script>

<style scoped>
button {
    width: 100%;
}
div.error-message {
    color: red;
}
</style>