import request from '@/utils/request'

// 查询接头盒纤芯状况
export function coreConnectShowApi(data) {
  return request({
    url: '/fiberedit/fiberdite-fiberline/TconId',
    method: 'get',
    params: data
  })
}

// 新增纤芯
export function addLinkApi(data) {
  return request({
    url: '/fiberedit/fiberdite-fiberline',
    method: 'post',
    data: data
  })
}

// 删除纤芯
export function delLinkApi(data) {
  return request({
    url: '/fiberedit/fiberdite-fiberline/id',
    method: 'get',
    params: data
  })
}

// 批量连接光缆
export function commitbatchConnectionsApi(data) {
  return request({
    url: '/fiberedit/fiberdite-fiberline/updateMore',
    method: 'get',
    params: data
  })
}

// 批量删除光缆
export function delMoreApi(data) {
  return request({
    url: '/fiberedit/fiberdite-fiberline/deleteMore',
    method: 'get',
    params: data
  })
}

// 批量删除光缆
export function saveAndCreateFiberApi(data) {
  return request({
    url: '/fiberedit/fiberdite-fiberline/getFiber',
    method: 'get',
    params: data
  })
}

// 获取光缆数据
export function getFiberDataApi(data) {
  return request({
    url: '/fiberedit/fiberedit-fiber/getFiberTopoInfo',
    method: 'get',
    params: data
  })
}
