import request from '@/utils/request'


export function obtainTheOpticalWiringConnectionRelationship(params) {
  return request({
    url: '/resources/SgTcconTcphylinkR/obtainTheOpticalWiringConnectionRelationship',
    method: 'get',
    params:params,
  })
}
export function saveTheOpticalWiringConnectionRelationship(data,params) {
  return request({
    url: '/resources/SgTcconTcphylinkR/saveTheOpticalWiringConnectionRelationship',
    method: 'post',
    data: data,
    params:params,
  })
}

//获取光缆的ODF配线模块
export function obtainODFWiringModuleForOpticalCable(data) {
  return request({
    url: '/resources/SgTcconTcphylinkR/obtainODFWiringModuleForOpticalCable',
    method: 'post',
    data: data
  })
}


//获取ODF配线模块的端子
export function obtainTheTerminalsOfTheODFWiringModule(data) {
  return request({
    url: '/resources/SgTcconTcphylinkR/obtainTheTerminalsOfTheODFWiringModule',
    method: 'post',
    data: data
  })
}
