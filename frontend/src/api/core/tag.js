import request from '@/utils/request'
export default {
  dict() {
    return request({
      url: '/admin/core/tag/dictList',
      method: 'get'
    })
  },
  list(page, limit, tagName) {
    return request({
      url: `/tags/list/${page}/${limit}`,
      method: 'get',
      params: { tagName: tagName }
    })
  },
  removeById(id) {
    return request({
      url: `/tags/remove/${id}`,
      method: 'delete'
    })
  },
  approval(tag) {
    return request({
      url: '/tags/save',
      method: 'post',
      data: tag
    })
  },

  getById(id) {
    return request({
      url: `/tags/getById/${id}`,
      method: 'get'
    })
  },
  updateById(tag) {
    return request({
      url: '/tags/update',
      method: 'put',
      data: tag
    })
  },
  stickyBlogById(id) {
    return request({
      url: `/tags/stickyBlogById/${id}`,
      method: 'PUT'
    })
  },
  deleteBatchTag(params) {
    return request({
      url: '/tags/deleteBatch',
      method: 'post',
      data: params
    })
  }
}
