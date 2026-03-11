import request from '@/utils/request'

export function getChannelTopoApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelsegB/queryTopoByChannelsegId',
    method: 'get',
    params:data
  })
}
export function getBuzTopoApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelsegB/queryTopoByBuzId',
    method: 'get',
    params:data
  })
}

export function getBuzTopoApi2(data) {
  return request({
    url: '/resources/sgTcconTcchannelsegB/queryTopoByBuzId2',
    method: 'get',
    params:data
  })
}
export function getLightPathByResIdApi(data) {
  return request({
    url: '/resources/tcopticalroadB/list',
    method: 'get',
    params:data
  })
}
export function submitLightPathApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelsegB/saveChannelsegTopo',
    method: 'post',
    data:data
  })
}
export function synchronizationLightPath(params) {
  return request({
    url: '/dataaccess/datatransfer/synchronizationLightPath',
    method: 'get',
    params:params
  })
}

