import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

createApp(App).use(store).use(router).mount('#app')
document.title = 'King of Bots';
// new Vue({
//     router,
//     store,
//     render: h => h(App)
// }).$mount('#app');
