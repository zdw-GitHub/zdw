import request from '@/utils/request'

// 新增光缆和接头盒关联
export function addSgTcconFiberRFbrconnect(data) {
  return request({
    url: '/resources/SgTcconFiberRFbrconnect',
    method: 'post',
    data: data
  })
}

// 删除光缆和接头盒关联
export function delSgTcconFiberRFbrconnect(id) {
  return request({
    url: '/resources/SgTcconFiberRFbrconnect/' + id,
    method: 'delete'
  })
}

// 查询光缆和接头盒关系根据光缆Id
export function listByFiberId(params) {
  return request({
    url: '/resources/SgTcconFiberRFbrconnect/listByFiberId',
    method: 'get',
    params:params
  })
}

// 查询类型为接头盒的光缆接续装置基本信息列表
export function jointBoxList(params) {
  return request({
    url: '/resources/SgTcconFiberRFbrconnect/jointBoxList',
    method: 'get',
    params:params
  })
}

// 光缆断点定位
export function fiberBreakpointLocation(params) {
  return request({
    url: '/resources/SgTcconFiberRFbrconnect/fiberBreakpointLocation',
    method: 'post',
    params:params
  })
}

// 新增接头盒
export function addSgTcconFbrconnectB(data) {
  return request({
    url: '/resources/SgTcconFbrconnectB',
    method: 'post',
    data: data
  })
}
// 修改接头盒
export function updateSgTcconFbrconnectB(data) {
  return request({
    url: '/resources/SgTcconFbrconnectB',
    method: 'put',
    data: data
  })
}

// 删除接头盒
export function delSgTcconFbrconnectB(id) {
  return request({
    url: '/resources/SgTcconFbrconnectB/' + id,
    method: 'delete'
  })
}
