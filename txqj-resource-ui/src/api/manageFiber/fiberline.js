import request from '@/utils/request'

// 提交修改
export function submitEdit(data) {
  return request({
    url: '/fiberline',
    method: 'put',
    data: data
  })
}



export function getFiberlineAwaitData(data) {
  return request({
    url: '/fiberline/listslave',
    method: 'get',
    params:data
  })
}
