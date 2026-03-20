import request from '@/utils/request'

// 根据光缆号获取光缆承载光路
export function getCarryingOpticalPathById(data) {
  return request({
    url: '/resCommon/selectCarryingOpticalPathById',
    method: 'post',
    data:data
  })
}

// 根据设备获取光缆承载光路
export function getSDHCarryingOpticalPathById(data) {
  return request({
    url: '/resCommon/selectSDHCarryingOpticalPathById',
    method: 'post',
    data:data
  })
}
// 根据板卡获取光缆承载光路
export function getBKCarryingOpticalPathById(data) {
  return request({
    url: '/resCommon/selectBKCarryingOpticalPathById',
    method: 'post',
    data:data
  })
}
// 根据板卡获取光缆承载光路
export function selectBuzCarryingOpticalPathById(data) {
  return request({
    url: '/resCommon/selectBuzCarryingOpticalPathById',
    method: 'post',
    data:data
  })
}
export function selectLineSn(data) {
  return request({
    url: '/resCommon/selectLineSn',
    method: 'post',
    data:data
  })
}
export function analysisFiberOpticCableCarryingOpticalPath(params) {
  return request({
    url: '/DataDetailInformation/analysisFiberOpticCableCarryingOpticalPath',
    method: 'get',
    params:params
  })
}

/*承载光路导出*/
export function carryingOpticalPathExport(data,query) {
  return request({
    url:'/resCommon/carryingOpticalPathExport',
    method: 'post',
    data: data,
    params: query,
    timeout: 1000*60*10
  })
}

