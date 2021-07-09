import request from '@/utils/request'

export function list(page, limit, title) {
  return request({
    url: `/article/list/${page}/${limit}/${title}`,
    method: 'get',
  })
}
export function del(id) {
  return request({
    url: `/article/delete/${id}`,
    method: 'delete',
  })
}
