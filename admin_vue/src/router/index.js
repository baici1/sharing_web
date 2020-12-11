import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import User from '../views/user/User.vue'
import Blog from '../views/blog/Blog.vue'
import Welcome from '../views/Welcome.vue'
import Category from '../views/category/Category.vue'
Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/Login' },
  {
    path: '/Login',
    name: 'Login',
    component: Login
  },
  {
    path: '/Home',
    name: 'Home',
    component: Home,
    redirect: '/welcome',
    children: [
      { path: '/Welcome', component: Welcome },
      // 通过路由展示用户组件
      { path: '/User', component: User },
      { path: '/Blog', component: Blog },
      { path: '/Category', component: Category }
    ]
  }

]

const router = new VueRouter({
  routes
})

export default router
