// 引入axios的初始化模块
import request from '@/utils/request'
export default {
  getCategoryLabels() {
    return request({
      url: '/article/getTagCategory',
      method: 'get'
    })
  },
  dict() {
    return request({
      url: '/admin/core/blog/dictList',
      method: 'get'
    })
  },
  save(blog) {
    return request({
      url: '/article/add',
      method: 'post',
      data: blog
    })
  },
  list(page, limit, title) {
    return request({
      url: `/article/list/${page}/${limit}`,
      method: 'get',
      params: { title: title }
    })
  },
  ulist(page, limit, title) {
    return request({
      url: `/user/list/${page}/${limit}`,
      method: 'get',
      params: { name: title }
    })
  },
  removeById(id) {
    return request({
      url: `/article/delete/${id}`,
      method: 'delete'
    })
  },
  getById(id) {
    return request({
      url: `/article/details/${id}`,
      method: 'get'
    })
  },
  updateById(blog) {
    return request({
      url: '/article/update',
      method: 'put',
      data: blog
    })
  }
}
