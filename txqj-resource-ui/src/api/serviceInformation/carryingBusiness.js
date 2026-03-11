import request from '@/utils/request'

// 根据光缆号获取光缆承载业务
export function getCarryingBusinessById(data) {
  return request({
    url: '/resCommon/selectCarryingBusinessById',
    method: 'post',
    data: data,
  })
}
// 根据SDH号获取承载业务
export function getSDHCarryingBusinessById(data) {
  return request({
    url: '/resCommon/selectSDHCarryingBusinessById',
    method: 'post',
    data: data,
  })
}
// 根据光路号获取承载业务
export function getGLCarryingBusinessById(data) {
  return request({
    url: '/resCommon/selectGLCarryingBusinessById',
    method: 'post',
    data: data,
  })
}
// 根据板卡号获取承载业务
export function getBKCarryingBusinessById(data) {
  return request({
    url: '/resCommon/selectBKCarryingBusinessById',
    method: 'post',
    data: data,
  })
}
// 根据光缆号获取光缆承载通道链路
export function getCarryingChannelLinkById(data) {
  return request({
    url: '/resCommon/selectCarryingChannelLinkById',
    method: 'post',
    data: data,
  })
}
// 根据光路号获取光缆承载通道链路
export function selectCarryingChannelLinkByGlId(data) {
  return request({
    url: '/resCommon/selectCarryingChannelLinkByGlId',
    method: 'post',
    data: data,
  })
}
// 根据SDH号获取光缆承载通道链路
export function selectCarryingChannelLinkBySDHId(data) {
  return request({
    url: '/resCommon/selectCarryingChannelLinkBySDHId',
    method: 'post',
    data: data,
  })
}
// 根据业务号获取光缆承载通道链路
export function selectCarryingChannelLinkByBuzId(data) {
  return request({
    url: '/resCommon/selectCarryingChannelLinkByBuzId',
    method: 'post',
    data: data,
  })
}
/*承载业务导出*/
export function carryingBusinessExport(data,query) {
  return request({
    url:'/resCommon/carryingBusinessExport',
    method: 'post',
    data: data,
    params: query,
    timeout: 1000*60*10
  })
}
