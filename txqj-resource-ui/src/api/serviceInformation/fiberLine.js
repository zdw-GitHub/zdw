import request from '@/utils/request'

export function getRouterApi(data) {
  return request({
    url: '/sgTcconTcchannelFiberB/getLineTopoByChannelsegId',
    method: 'get',
    params:data
  })
}

export function getLineTopoByTDId(data) {
  return request({
    url: '/sgTcconTcchannelFiberB/getLineTopoByTDId',
    method: 'get',
    params:data
  })
}

export function saveRouterApi(data) {
  return request({
    url: '/sgTcconTcchannelFiberB/saveChannelLineTopo',
    method: 'post',
    data:data
  })
}

export function getFiberOptionsApi(data) {
  return request({
    url: '/fiber/listmeta',
    method: 'get',
    params:data
  })
}
export function getFiberLineOptionsApi(data) {
  return request({
    url: '/fiberline/listZs',
    method: 'get',
    params:data
  })
}
