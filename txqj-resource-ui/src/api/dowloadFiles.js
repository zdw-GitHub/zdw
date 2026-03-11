import request from '@/utils/request'


// 获取用户详细信息
export function downloadOpt(url) {
  return request({
    url: url,
    // 不加报403
    headers: {
      isToken: false
    },
    responseType: 'blob',
    method: 'get'
  })
}
