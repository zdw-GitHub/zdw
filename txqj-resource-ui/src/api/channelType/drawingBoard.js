import request from '@/utils/request'

// 根据站点获取图形
export function getPipeDataApi (query, url) {
  return request({
    url: '/resources' + url,
    method: 'get',
    params: query
  })
}

// 更新站点图形
export function setPipeDataApi (data, url) {
  return request({
    url: '/resources' + url,
    method: 'post',
    data: data
  })
}

// 获取沟道关联光缆
export function getChannelFiberApi (query) {
  return request({
    url: '/resources/pipe/queryimgsegbypipe',
    method: 'get',
    params: query
  })
}

// 清空图形内容
export function delPipeDataApi (url) {
  return request({
    url: '/resources' + url,
    method: 'delete',
  })
}