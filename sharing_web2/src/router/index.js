import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login/Login.vue'
import Register from '../views/Login/Register.vue'
import Blogs from '../views/Blogs.vue'
import BlogEdit from '../views/Edit/BlogEdit.vue'
import Loginhome from '../views/Login/Loginhome.vue'
import UserEdithome from '../views/Edit/UserEdithome.vue'
import UserEdit from '../views/Edit/UserEdit.vue'
import PassEdit from '../views/Edit/PassEdit.vue'
import ForgetPw from '../views/Login/ForgetPw.vue'
import BlogDetail from '../views/BlogDetail.vue'
import User from '../views/User/User.vue'
import Collect from '../views/User/Collect.vue'
Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/Loginhome' },
  {
    path: '/Loginhome',
    component: Loginhome,
    redirect: '/Login',
    children: [
      { path: '/Register', component: Register },
      { path: '/Login', component: Login },
      { path: '/ForgetPw', component: ForgetPw }
    ]
  },
  {
    path: '/Blogs',
    component: Blogs
  },
  {
    path: '/blog/add',
    name: 'BlogAdd',
    component: BlogEdit
  },
  {
    path: '/blog/:blogId',
    name: 'BlogDetail',
    component: BlogDetail
  },
  {
    path: '/blog/:blogId/edit',
    name: 'BlogEdit',
    component: BlogEdit
  },
  {
    path: '/UserEdithome',
    name: 'UserEdithome',
    component: UserEdithome,
    children: [
      { path: '/UserEdit', component: UserEdit },
      { path: '/PassEdit', component: PassEdit }
    ]
  },
  {
    path: '/User',
    name: 'User',
    component: User
  },
  {
    path: '/Collect',
    name: 'Collect',
    component: Collect
  }
]

const router = new VueRouter({
  routes
})

export default router
