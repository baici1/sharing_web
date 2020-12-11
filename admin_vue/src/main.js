import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
// 全局样式表
import './assets/css/global.css'
// 导入字体图标
import './assets/font_2008828_yz5l4oqjqmg/iconfont.css'
import './axios.js' // 请求拦截
import axios from 'axios'

import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// 配置请求根路径

// markdown编辑器
// use
Vue.use(mavonEditor)

Vue.config.productionTip = false
Vue.prototype.$http = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
