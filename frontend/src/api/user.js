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
        url: '/user/logout',
        method: 'post'
    })
}

export function getUserList(page, limit, name) {
    return request({
        url: `/user/list/${page}/${limit}`,
        method: 'get',
        params: { name: name }
    })
}

export function deleteRequest(id) {
    return request({
        url: `/user/remove/${id}`,
        method: 'DELETE'
    })
}

export function userEnabled(id) {
    return request({
        url: `/user/update/${id}`,
        method: 'PUT'
    })
}

export function saveUser(data) {
    return request({
        url: "/user/save",
        method: 'POST',
        data
    })
}