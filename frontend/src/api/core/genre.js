import request from '@/utils/request'
export default {
    dict() {
        return request({
            url: '/category/dictList',
            method: 'get'
        })
    },
    list(page, limit, searchObj) {
        return request({
            url: `/category/list/${page}/${limit}`,
            method: 'get',
            params: searchObj
        })
    },
    removeById(id) {
        return request({
            url: `/category/remove/${id}`,
            method: 'delete'
        })
    },
    approval(tag) {
        return request({
            url: '/category/save',
            method: 'post',
            data: tag
        })
    },

    getById(id) {
        return request({
            url: `/category/getById/${id}`,
            method: 'get'
        })
    },
    updateById(tag) {
        return request({
            url: '/category/update',
            method: 'put',
            data: tag
        })
    },
    stickyBlogById(id) {
        return request({
            url: `/category/stickyBlogById/${id}`,
            method: 'put'
        })
    },
    deleteBatchTag(params) {
        return request({
            url: '/category/deleteBatch',
            method: 'post',
            data: params
        })
    },
  getUserList(page, limit) {
    return request({
      url: `/user/list/${page}/${limit}`,
      method: 'get',
    })
  },

  listOrderByDesc(page, limit, field) {
    return request({
      url: `/article/listOrderByDESC/${page}/${limit}/${field}`,
      method: 'get',
    })
  },
  getCategory(page, limit) {
    return request({
      url: `/category/list/${page}/${limit}`,
      method: 'get',
    })
  }

}
