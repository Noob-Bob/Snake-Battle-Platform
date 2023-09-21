<template>
    <ContentField>
        Register
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                      <label for="username" class="form-label">Username</label>
                      <input v-model="username" type="text" class="form-control" id="username" aria-describedby="usernameHelp">
                      <div id="usernameHelp" class="form-text">Please input your username.</div>
                    </div>
                    <div class="mb-3">
                      <label for="Password" class="form-label">Password</label>
                      <input v-model="password" type="password" class="form-control" id="Password1">
                      <div id="usernameHelp" class="form-text">Please input your password.</div>
                    </div>
                    <div class="mb-3">
                        <!--reinput the password-->
                        <label for="Password" class="form-label">Confirm Password</label>
                        <input v-model="confirmedPassword" type="password" class="form-control" id="Password2">
                        <div id="usernameHelp" class="form-text">Please input your password again.</div>
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
import { ref } from 'vue'
import router from '../../../router/index'
import $ from 'jquery'
export default {
    components: {
        ContentField
    },
    setup() {
        let username = ref('');
        let password = ref('');
        let confirmedPassword = ref('');
        let error_message = ref('');

        const register = () => {
            $.ajax({ // get data from backend
                url: "https://app2079.acapp.acwing.com.cn/api/user/account/register/",
                // url: "http://127.0.0.1:3000/api/user/account/register/",
                type: "post",
                data: {
                    username: username.value,
                    password: password.value,
                    confirmedPassword: confirmedPassword.value,
                },
                success(resp) { // key: "success", value: a function named success
                    console.log(resp);
                    if (resp.error_message === "success") { // if successfully registered, then jump to login page
                        router.push({name: "user_account_login"});
                    } else {
                        error_message.value = resp.error_message;
                    }
                },
                // "success": function(resp)
            });
        }

        return {
            username,
            password,
            confirmedPassword,
            error_message,
            register
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