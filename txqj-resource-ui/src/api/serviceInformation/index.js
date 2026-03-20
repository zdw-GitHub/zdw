import request from '@/utils/request'

export function selectTcchanne1BByYWIDAPI(data) {
  return request({
    url: '/sgTcconTcchannelB/selectTcchannelBByYWID',
    method: 'get',
    params:data
  })
}
export function queryTheBoundLinkBasedOnTheBusinessID(data) {
  return request({
    url: '/sgTcconTcchannelB/queryTheBoundLinkBasedOnTheBusinessID',
    method: 'get',
    params:data
  })
}

export function deleteRemovetcchannelB(id) {
  return request({
    url: '/sgTcconTcchannelB/removetcchannelB/'+ id,
    method: 'delete',
  })
}
// 光路增删改查
export function getTtdTableApi(data) {
  return request({
    url: '/sgTcconTcchannelsegB/selectTcchannelsegB',
    method: 'get',
    params:data
  })
}
export function ttdAddCommitApi(data) {
  return request({
    url: '/sgTcconTcchannelsegB/insert',
    method: 'post',
    data:data
  })
}
export function ttdEditCommitApi(data) {
  return request({
    url: '/sgTcconTcchannelsegB',
    method: 'put',
    data:data
  })
}

export function ttdDelCommitApi(id) {
  return request({
    url: '/sgTcconTcchannelsegB/removeTcchannelsegB/' + id,
    method: 'delete',
  })
}
export function ywDelCommitApi(id) {
  return request({
    url: '/sgTcconTcbuzB/' + id,
    method: 'delete',
  })
}

export function tdllAddCommitApi(data) {
  return request({
    url: '/sgTcconTcchannelB/insert',
    method: 'post',
    data:data
  })
}
export function tdllEditCommitApi(data) {
  return request({
    url: '/sgTcconTcchannelB/editchannel',
    method: 'put',
    data:data
  })
}
export function bindChannelSegments(params) {
  return request({
    url: '/sgTcconTcchannelsegB/bindChannelSegments',
    method: 'get',
    params:params
  })
}
export function bindingFiberCoreChannels(params) {
  return request({
    url: '/sgTcconTcchannelFiberB/bindingFiberCoreChannels',
    method: 'get',
    params:params
  })
}




// 纤芯通道增删改查

export function getGlTableApi(data) {
  return request({
    url: '/sgTcconTcchannelFiberB/list',
    method: 'get',
    params:data
  })
}
export function bindingList(data) {
  return request({
    url: '/sgTcconTcchannelFiberB/bindingList',
    method: 'get',
    params:data
  })
}
export function bindingTTDList(data) {
  return request({
    url: '/sgTcconTcchannelsegB/bindingTTDList',
    method: 'get',
    params:data
  })
}
export function bindingTTDApi(data) {
  return request({
    url: '/tcchannelseg',
    method: 'post',
    data:data
  })
}
export function unbindTTDApi(data) {
  return request({
    url: '/tcchannelseg/removeBySegId',
    method: 'post',
    params:data
  })
}
export function GlAddCommitApi(data) {
  return request({
    url: '/sgTcconTcchannelFiberB/addChannelFiber',
    method: 'post',
    data:data
  })
}
export function GlEditCommitApi(data) {
  return request({
    url: '/sgTcconTcchannelFiberB/editChannelFiber',
    method: 'put',
    data:data
  })
}


export function GlDelCommitApi(id) {
  return request({
    url: '/sgTcconTcchannelFiberB/' + id,
    method: 'delete',
  })
}

export function getbindLlApi(data) {
  return request({
    url: '/sgTcconTcchannelB/Channellist',
    method: 'get',
    params:data
  })
}

export function bindLlCommitApi(data) {
  return request({
    url: '/sgTcconTcbuzRTcchannel/addBuzRChannel',
    method: 'post',
    data:data
  })
}

export function unbindLlCommitApi(data) {
  return request({
    url: '/sgTcconTcbuzRTcchannel/removedata',
    method: 'delete',
    data:data
  })
}
export function addbuz(data) {
  return request({
    url: '/sgTcconTcbuzB/addbuz',
    method: 'post',
    data:data
  })
}
export function editbuz(data) {
  return request({
    url: '/sgTcconTcbuzB/editbuz',
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
    url:'/DataDetailInformation/serialServiceRouting',
    method: 'get',
    params:params
  })
}
