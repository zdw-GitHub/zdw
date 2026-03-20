import request from '@/utils/request'

// 提交新增
export function submit(data) {
  return request({
    url: '/fiber',
    method: 'post',
    data: data
  })
}

// 提交修改
export function submitEdit(data) {
  return request({
    url: '/fiber',
    method: 'put',
    data: data
  })
}

// 提交删除
// export function delData(id) {
//   return request({
//     url: '/fiber/' + id,
//     method: 'delete',
//   })
// }

// 提交删除
export function delData(data) {
  return request({
    url: '/fiber/removeOne',
    method: 'delete',
    data: data
  })
}

export function batchDelData(data) {
  return request({
    url: '/fiber/'+data,
    method: 'delete',
  })
}

// 获取待处理光缆列表
  export function getFiberData(params) {
    return request({
      url: '/fiber/listslave',
      method: 'get',
      params:params
    })
  }


// 光缆路由
export function getFiberRouter(params) {
  return request({
    url: '/fiber/selectFiberTopoMeta',
    method: 'get',
    params:params
  })
}
// 光缆路由
export function getFiberRouterAwait(params) {
  return request({
    url: '/fiber/selectFiberTopoMiddle',
    method: 'get',
    params:params
  })
}

// 查询接头盒纤芯状况
export function coreConnectShowApi(data) {
  return request({
    url: '/lineline/getTconId',
    method: 'get',
    params: data
  })
}

// 查询接头盒纤芯状况
export function creatFiberLineForMeda(data) {
  return request({
    url: '/fiber/creatFiberLineForMeda',
    method: 'post',
    data: data
  })
}

// 查询接头盒纤芯状况
export function creatFiberLineForMiddle(data) {
  return request({
    url: '/fiber/creatFiberLineForMiddle',
    method: 'post',
    data: data
  })
}

// 查询接头盒纤芯状况
export function addFiberLineApi(data) {
  return request({
    url: '/lineline',
    method: 'post',
    data: data
  })
}

// 查询接头盒纤芯状况
export function delFiberLinkApi(data) {
  return request({
    url: '/lineline/remove',
    method: 'delete',
    data: data
  })
}

// 查询接头盒纤芯状况
export function getAwaitFiberByIdApi(data) {
  return request({
    url: '/fiber/getFiberObj',
    method: 'get',
    params: data
  })
}

// 获取光缆审批任务ID
export function getFiberApprovalID(params) {
  return request({
    url: '/fiber/selectFiberApprovalID',
    method: 'get',
    params:params
  })
}
/*光缆承载光路导出*/
export function exportOpticalCableLoadBearingLightPath(data) {
  return request({
    url:'/resCommon/opticalCableLoadBearingLightPathExport',
    method: 'post',
    data: data,
    timeout: 1000*60*10
  })
}

export function selectFiberIdByHgsId(params) {
  return request({
    url: '/fiber/selectFiberIdByHgsId',
    method: 'get',
    params:params
  })
}
export function selectTaskIdByHgsId(params) {
  return request({
    url: '/fiber/selectTaskIdByHgsId',
    method: 'get',
    params:params
  })
}

export function generateFiberOpticCableCores(params) {
  return request({
    url: '/DataDetailInformation/generateFiberOpticCableCores',
    method: 'get',
    params:params
  })
}
export function generateSgTcconFiberlineRFibersegline(params) {
  return request({
    url: '/DataDetailInformation/generateSgTcconFiberlineRFibersegline',
    method: 'get',
    params:params
  })
}
export function generateFiberGraphical(params) {
  return request({
    url: '/DataDetailInformation/generateFiberGraphical',
    method: 'get',
    params:params
  })
}

export function updateFiberLineNumber(params) {
  return request({
    url: '/fiber/updateFiberLineNumber',
    method: 'post',
    params:params
  })
}
