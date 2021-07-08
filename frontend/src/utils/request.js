import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import router from '@/router'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 30000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom status
   * Here is just an example
   * You can also judge the status by HTTP Status status
   */
  response => {
    const res = response.data

    // if the custom status is not 20000, it is judged as an error.
    switch (res.status) {
      case 200:
        return res

      case 403:
        Message({
          message: '请重新登录',
          type: 'error',
          duration: 5 * 1000
        })
        router.push({ path: '/login' })
        break

      default:
        Message({
          message: res.msg || 'Error',
          type: 'error',
          duration: 5 * 1000
        })

        return Promise.reject(new Error(res.msg || 'Error'))
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: '请求服务器错误',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service