import { createRouter, createWebHistory } from 'vue-router'
import PkindexView from '../views/pk/PkindexView'
import RanklistindexView from '../views/ranklist/RanklistindexView'
import RecordindexView from '../views/record/RecordindexView'
import RecordContentView from '../views/record/RecordContentView'
import UserbotindexView from '../views/user/bot/UserbotindexView'
import NotFound from '../views/error/NotFound'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'
import store from '../store/index'
const routes = [
  // redirect the root url to the pk page
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
    meta: { // whether require auth
      requestAuth: true,
    }
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkindexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordindexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path:"/record/:recordId/",
    name: "record_content",
    component: RecordContentView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistindexView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserbotindexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/"
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * to: target page
 * from: from page
 * next: operation needed
 */
router.beforeEach((to, from, next) => {
  // if the targer page require auth and the current user has not auth yet
  // then redirect to the login page
  // else do nothing
  if (to.meta.requestAuth && !store.state.user.is_login) {
    next({name: "user_account_login"});
  } else {
    next();
  }
})

export default router
