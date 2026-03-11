import request from '@/utils/request'

export function selectTcchanne1BByYWIDAPI(data) {
  return request({
    url: '/resources/sgTcconTcchannelB/selectTcchannelBByYWID',
    method: 'get',
    params:data
  })
}
export function queryTheBoundLinkBasedOnTheBusinessID(data) {
  return request({
    url: '/resources/sgTcconTcchannelB/queryTheBoundLinkBasedOnTheBusinessID',
    method: 'get',
    params:data
  })
}

export function deleteRemovetcchannelB(id) {
  return request({
    url: '/resources/sgTcconTcchannelB/removetcchannelB/'+ id,
    method: 'delete',
  })
}
// 光路增删改查
export function getTtdTableApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelsegB/selectTcchannelsegB',
    method: 'get',
    params:data
  })
}
export function ttdAddCommitApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelsegB/insert',
    method: 'post',
    data:data
  })
}
export function ttdEditCommitApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelsegB',
    method: 'put',
    data:data
  })
}

export function ttdDelCommitApi(id) {
  return request({
    url: '/resources/sgTcconTcchannelsegB/removeTcchannelsegB/' + id,
    method: 'delete',
  })
}
export function ywDelCommitApi(id) {
  return request({
    url: '/resources/sgTcconTcbuzB/' + id,
    method: 'delete',
  })
}

export function tdllAddCommitApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelB/insert',
    method: 'post',
    data:data
  })
}
export function tdllEditCommitApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelB/editchannel',
    method: 'put',
    data:data
  })
}
export function bindChannelSegments(params) {
  return request({
    url: '/resources/sgTcconTcchannelsegB/bindChannelSegments',
    method: 'get',
    params:params
  })
}
export function bindingFiberCoreChannels(params) {
  return request({
    url: '/resources/sgTcconTcchannelFiberB/bindingFiberCoreChannels',
    method: 'get',
    params:params
  })
}




// 纤芯通道增删改查

export function getGlTableApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelFiberB/list',
    method: 'get',
    params:data
  })
}
export function bindingList(data) {
  return request({
    url: '/resources/sgTcconTcchannelFiberB/bindingList',
    method: 'get',
    params:data
  })
}
export function bindingTTDList(data) {
  return request({
    url: '/resources/sgTcconTcchannelsegB/bindingTTDList',
    method: 'get',
    params:data
  })
}
export function bindingTTDApi(data) {
  return request({
    url: '/resources/tcchannelseg',
    method: 'post',
    data:data
  })
}
export function unbindTTDApi(data) {
  return request({
    url: '/resources/tcchannelseg/removeBySegId',
    method: 'post',
    params:data
  })
}
export function GlAddCommitApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelFiberB/addChannelFiber',
    method: 'post',
    data:data
  })
}
export function GlEditCommitApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelFiberB/editChannelFiber',
    method: 'put',
    data:data
  })
}


export function GlDelCommitApi(id) {
  return request({
    url: '/resources/sgTcconTcchannelFiberB/' + id,
    method: 'delete',
  })
}

export function getbindLlApi(data) {
  return request({
    url: '/resources/sgTcconTcchannelB/Channellist',
    method: 'get',
    params:data
  })
}

export function bindLlCommitApi(data) {
  return request({
    url: '/resources/sgTcconTcbuzRTcchannel/addBuzRChannel',
    method: 'post',
    data:data
  })
}

export function unbindLlCommitApi(data) {
  return request({
    url: '/resources/sgTcconTcbuzRTcchannel/removedata',
    method: 'delete',
    data:data
  })
}
export function addbuz(data) {
  return request({
    url: '/resources/sgTcconTcbuzB/addbuz',
    method: 'post',
    data:data
  })
}
export function editbuz(data) {
  return request({
    url: '/resources/sgTcconTcbuzB/editbuz',
    method: 'PUT',
    data:data
  })
}

export function reSyncByTms(params) {
  return request({
    url: '/dataaccess/datatransfer/datasyncBySegId',
    method: 'get',
    params: params
  })
}
export function computeBuzEmergencyRoute(params) {
  return request({
    url:'/analysis/PathAlgorithms/computeBuzEmergencyRoute',
    method: 'get',
    params:params
  })
}
export function serialServiceRouting(params) {
  return request({
    url:'/resources/DataDetailInformation/serialServiceRouting',
    method: 'get',
    params:params
  })
}
