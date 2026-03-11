import request from '@/utils/request'

// 提交删除
export function delDataApi(id) {
  return request({
    url: '/resources/tcopticalroadB/' + id,
    method: 'delete',
  })
}

// 提交光路修改
export function commitRouterApi(data) {
  return request({
    url: '/resources/routerrfiber/saverouter',
    method: 'post',
    data: data
  })
}
export function saveMsp(params) {
  return request({
    url: '/resources/routerrfiber/saveMsp',
    method: 'get',
    params:params
  })
}

// 光路列表导出
export function exportOpticalPath(data) {
  return request({
    url:'/resCommon/exportOpticalPath',
    method: 'post',
    data: data,
    timeout: 1000*60*10
  })
}
export function computeOpticalroadEmergencyRoute(params) {
  return request({
    url:'/analysis/PathAlgorithms/computeOpticalroadEmergencyRoute',
    method: 'get',
    params:params
  })
}

export function serialOpticalRouting(params) {
  return request({
    url:'/resources/DataDetailInformation/serialOpticalRouting',
    method: 'get',
    params:params
  })
}
