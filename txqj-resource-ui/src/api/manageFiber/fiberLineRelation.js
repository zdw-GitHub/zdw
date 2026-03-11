import request from '@/utils/request'

// 根据光缆号获取光缆关联的一次线路
export function getOneLine(data) {
  return request({
    url: '/resCommon/selectOneLineByFiberId',
    method: 'post',
    data: data
  })
}

// 根据光缆号获取光缆关联的一次线路
export function getNotOneLine(data,params) {
  return request({
    url: '/resCommon/selectOneLineNotReFiberId',
    method: 'post',
    data: data,
    params: params
  })
}
export function getSegByFiberId(params) {
  return request({
    url: '/resources/sgTcdevFibersegB/getSegByFiberId',
    method: 'get',
    params: params
  })
}
