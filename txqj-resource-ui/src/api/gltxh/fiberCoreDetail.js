import request from '@/utils/request'

// 查询接头盒纤芯状况
export function seeDetailsApi(params) {
  return request({
    url: '/fiberedit/fiberlinesegline/querylist',
    method: 'get',
    params: params
  })
}

// 修改纤芯
export function editCommitApi(data) {
  return request({
    url: '/fiberedit/fiberlinesegline/updatell',
    method: 'get',
    params: data
  })
}

// 修改 光缆段纤芯/光缆纤芯 保存
export function formEditCommitApi(url, data) {
  return request({
    url: url,
    method: 'put',
    data: data
  })
}

// 获取光缆纤芯和光缆段纤芯关系 list
export function querylist(params) {
  return request({
    url: '/fiberlinerfibersegline/querylist',
    method: 'get',
    params: params
  })
}

// 修改光缆段纤芯与光缆纤芯
export function updataSgTcdevFiberSegLineRLineRB(params) {
  return request({
    url: '/fiberlinerfibersegline/updataSgTcdevFiberSegLineRLineRB',
    method: 'get',
    params: params
  })
}


