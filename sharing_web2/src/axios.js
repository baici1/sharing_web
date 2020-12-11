import axios from 'axios'
import Element from 'element-ui'
import store from './store'
import router from './router'
axios.defaults.baseURL = 'http://localhost:8081'
axios.interceptors.request.use(config => {
  console.log('前置拦截')
  // 可以统一设置请求头
  return config
})
axios.interceptors.request.use(config => {
  // config.headers['Content-Type'] = 'application/x-www-form-urlencoded';
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.common.token = localStorage.getItem('token')
  }
  return config
}, err => {
  return Promise.reject(err)
})

axios.interceptors.response.use(response => {
  const res = response.data
  console.log('后置拦截')
  // 当结果的code是否为200的情况
  if (res.code === 200) {
    return response
  } else {
    // 弹窗异常信息
    Element.Message({
      message: response.data.msg,
      type: 'error',
      duration: 2 * 1000
    })
    // 直接拒绝往下面返回结果信息
    return Promise.reject(response.data.msg)
  }
},
error => {
  console.log('err' + error)// for debug
  // eslint-disable-next-line no-irregular-whitespace
  if (error.response.data) {
    // eslint-disable-next-line no-irregular-whitespace
    error.message = error.response.data.msg
  }
  // 根据请求状态觉得是否登录或者提示其他
  if (error.response.status === 404) {
    error.message = '获取信息失败!'
  }
  if (error.response.status === 400) {
    error.message = '操作失败!'
  }
  if (error.response.status === 401) {
    store.commit('REMOVE_INFO')
    router.push({
      path: '/login'
    })
    error.message = '请重新登录'
  }
  if (error.response.status === 403) {
    error.message = '权限不足，无法访问'
  }
  Element.Message({
    message: error.message,
    type: 'error',
    duration: 3 * 1000
  })
  return Promise.reject(error)
})
