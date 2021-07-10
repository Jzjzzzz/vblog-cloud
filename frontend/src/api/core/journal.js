import request from '@/utils/request'
export default {
  list(page, limit) {
    return request({
      url: `/journal/list/${page}/${limit}`,
      method: 'get'
    })
  }
}
