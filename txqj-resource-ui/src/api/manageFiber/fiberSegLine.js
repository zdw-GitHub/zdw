import request from '@/utils/request'

// 获取表格下拉框数据
export function getData(data) {
  return request({
    url: '/resources/fibersegline/listZs',
    method: 'get',
    params:data
  })
}

// 提交新增
export function submit(data) {
  return request({
    url: '/resources/fibersegline',
    method: 'post',
    data: data
  })
}


// 提交修改
export function submitEdit(data) {
  return request({
    url: '/resources/fibersegline',
    method: 'put',
    data: data
  })
}

export function delData(data) {
  return request({
    url: '/resources/fibersegline/remove',
    method: 'delete',
    data: data
  })
}

export function getFiberSegLineByFiberSegData(data) {
  return request({
    url: '/resources/fibersegline/listslave',
    method: 'get',
    params:data
  })
}

