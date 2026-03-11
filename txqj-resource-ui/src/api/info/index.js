import request from '@/utils/request'
export function getSdhBySiteId (data) {
  return request({
    url: '/resCommon/getSdhBySiteId',
    method: 'post',
    data: data
  })
}
export function getOtnBySiteId (data) {
  return request({
    url: '/resCommon/getOtnBySiteId',
    method: 'post',
    data: data
  })
}
export function getFiberBySiteId (data) {
  return request({
    url: '/resCommon/getFiberBySiteId',
    method: 'post',
    data: data
  })
}
export function getFiberByGlId (data) {
  return request({
    url: '/resCommon/getFiberByGlId',
    method: 'post',
    data: data
  })
}
export function getFiberByBuzId (data) {
  return request({
    url: '/resCommon/getFiberByBuzId',
    method: 'post',
    data: data
  })
}

export function getRoomListBySiteId (query) {
  return request({
    url: '/resCommon/getRoomListBySiteId',
    method: 'get',
    params:query
  })
}

export function getNeByRackId (query) {
  return request({
    url: '/resCommon/getNeByRackId',
    method: 'get',
    params:query
  })
}
