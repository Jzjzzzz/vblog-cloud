import request from '@/utils/request'
export default {
  getWebConfig(params) {
    return request({
      url: '/config/getWebConfig',
      method: 'get',
      params
    })
  },
  editWebConfig(params) {
    return request({
      url: '/config/editWebConfig',
      method: 'put',
      data: params
    })
  },
  uploadImage(data) {
    return request({
      url: '/file/upload',
      method: 'post',
      data: data
    })
  },
  delete(url) {
    return request({
      url: '/api/oss/fileoss/remove?url=' + url,
      method: 'delete'
    })
  }
}
