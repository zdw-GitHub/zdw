import request from '@/utils/request'

export function getFiberList(params) {
  return request({
    url: '/analysis/ymFiber/getFiberList',
    method: 'get',
    params:params
  })
}
export function getTyFiberList(params) {
  return request({
    url: '/analysis/ymFiber/getTyFiberList',
    method: 'get',
    params:params
  })
}
export function getFiberLine(params) {
  return request({
    url: '/analysis/ymFiber/getFiberLine',
    method: 'get',
    params:params
  })
}
export function association(params) {
  return request({
    url: '/analysis/ymFiber/association',
    method: 'post',
    params:params
  })
}
export function getOtdrData(params) {
  return request({
    url: '/analysis/ymFiber/getOtdrData',
    method: 'get',
    params:params
  })
}


