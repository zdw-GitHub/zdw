import request from '@/utils/request'

export function getRouterApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelFiberB/getLineTopoByChannelsegId',
    method: 'get',
    params:data
  })
}

export function getLineTopoByTDId(data) {
  return request({
    url: '/resources/sgTcconTcchannelFiberB/getLineTopoByTDId',
    method: 'get',
    params:data
  })
}

export function saveRouterApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelFiberB/saveChannelLineTopo',
    method: 'post',
    data:data
  })
}

export function getFiberOptionsApi(data) {
  return request({
    url: '/resources/fiber/listmeta',
    method: 'get',
    params:data
  })
}
export function getFiberLineOptionsApi(data) {
  return request({
    url: '/resources/fiberline/listZs',
    method: 'get',
    params:data
  })
}
