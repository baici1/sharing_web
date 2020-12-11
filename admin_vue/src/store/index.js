import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 属性
    token: '',
    userInfo: JSON.parse(sessionStorage.getItem('userInfo'))
  },
  mutations: {
    // set方法
    SET_TOKEN: (state, token) => {
      state.token = token
      // 存入浏览器里
      localStorage.setItem('token', token)
    },
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo
      // 存入浏览器里
      sessionStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    // 当退出时候删除token
    REMOVE_INFO: (state) => {
      state.token = ''
      state.userInfo = {}
      // 存入浏览器里
      localStorage.setItem('token', '')
      sessionStorage.setItem('userInfo', JSON.stringify(''))
    }
  },
  getters: {
    // get方法
    getUser: state => {
      return state.userInfo
    }
  },
  actions: {
  },
  modules: {
  }
})
