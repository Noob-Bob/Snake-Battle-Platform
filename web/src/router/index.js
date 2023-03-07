import { createRouter, createWebHistory } from 'vue-router'
import PkindexView from '../views/pk/PkindexView'
import RanklistindexView from '../views/ranklist/RanklistindexView'
import RecordindexView from '../views/record/RecordindexView'
import UserbotindexView from '../views/user/bot/UserbotindexView'
import NotFound from '../views/error/NotFound'
const routes = [
  // redirect the root url to the pk page
  {
    path: "/",
    name: "home",
    redirect: "/pk/"
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkindexView
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordindexView
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistindexView
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserbotindexView
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound
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

export default router
