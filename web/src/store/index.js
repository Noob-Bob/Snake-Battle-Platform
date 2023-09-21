import { createStore } from 'vuex'
import ModuleUser from './user' // add user.js to global field
import ModulePK from './pk'
import ModuleRecord from './record'

export default createStore({
  state: {
  },
  // mutation is the only way to modify state
  // it does not care business logic, but only cares about state
  mutations: {
  },
  // action is business logic
  // action cannot commit more than 1 mutations at a time
  // action does not care state
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: ModulePK,
    record: ModuleRecord
  }
})
