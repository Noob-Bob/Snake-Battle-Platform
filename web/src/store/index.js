import { createStore } from 'vuex'
import ModuleUser from './user'
import ModulePK from './pk'
import ModuleRecord from './record'

export default createStore({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: ModulePK,
    record: ModuleRecord
  }
})
