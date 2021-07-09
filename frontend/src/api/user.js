import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/user/my',
    method: 'get',
  })
}

export function logout() {
  return request({
    baseURL: '/dev-api',
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
