import request from '@/utils/request'

// 提交新增
export function submit(data) {
  return request({
    url: '/resources/fiber',
    method: 'post',
    data: data
  })
}

// 提交修改
export function submitEdit(data) {
  return request({
    url: '/resources/fiber',
    method: 'put',
    data: data
  })
}

// 提交删除
// export function delData(id) {
//   return request({
//     url: '/resources/fiber/' + id,
//     method: 'delete',
//   })
// }

// 提交删除
export function delData(data) {
  return request({
    url: '/resources/fiber/removeOne',
    method: 'delete',
    data: data
  })
}

export function batchDelData(data) {
  return request({
    url: '/resources/fiber/'+data,
    method: 'delete',
  })
}

// 获取待处理光缆列表
  export function getFiberData(params) {
    return request({
      url: '/resources/fiber/listslave',
      method: 'get',
      params:params
    })
  }


// 光缆路由
export function getFiberRouter(params) {
  return request({
    url: '/resources/fiber/selectFiberTopoMeta',
    method: 'get',
    params:params
  })
}
// 光缆路由
export function getFiberRouterAwait(params) {
  return request({
    url: '/resources/fiber/selectFiberTopoMiddle',
    method: 'get',
    params:params
  })
}

// 查询接头盒纤芯状况
export function coreConnectShowApi(data) {
  return request({
    url: '/resources/lineline/getTconId',
    method: 'get',
    params: data
  })
}

// 查询接头盒纤芯状况
export function creatFiberLineForMeda(data) {
  return request({
    url: '/resources/fiber/creatFiberLineForMeda',
    method: 'post',
    data: data
  })
}

// 查询接头盒纤芯状况
export function creatFiberLineForMiddle(data) {
  return request({
    url: '/resources/fiber/creatFiberLineForMiddle',
    method: 'post',
    data: data
  })
}

// 查询接头盒纤芯状况
export function addFiberLineApi(data) {
  return request({
    url: '/resources/lineline',
    method: 'post',
    data: data
  })
}

// 查询接头盒纤芯状况
export function delFiberLinkApi(data) {
  return request({
    url: '/resources/lineline/remove',
    method: 'delete',
    data: data
  })
}

// 查询接头盒纤芯状况
export function getAwaitFiberByIdApi(data) {
  return request({
    url: '/resources/fiber/getFiberObj',
    method: 'get',
    params: data
  })
}

// 获取光缆审批任务ID
export function getFiberApprovalID(params) {
  return request({
    url: '/resources/fiber/selectFiberApprovalID',
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
    url: '/resources/fiber/selectFiberIdByHgsId',
    method: 'get',
    params:params
  })
}
export function selectTaskIdByHgsId(params) {
  return request({
    url: '/resources/fiber/selectTaskIdByHgsId',
    method: 'get',
    params:params
  })
}

export function generateFiberOpticCableCores(params) {
  return request({
    url: '/resources/DataDetailInformation/generateFiberOpticCableCores',
    method: 'get',
    params:params
  })
}
export function generateSgTcconFiberlineRFibersegline(params) {
  return request({
    url: '/resources/DataDetailInformation/generateSgTcconFiberlineRFibersegline',
    method: 'get',
    params:params
  })
}
export function generateFiberGraphical(params) {
  return request({
    url: '/resources/DataDetailInformation/generateFiberGraphical',
    method: 'get',
    params:params
  })
}

export function updateFiberLineNumber(params) {
  return request({
    url: '/resources/fiber/updateFiberLineNumber',
    method: 'post',
    params:params
  })
}
