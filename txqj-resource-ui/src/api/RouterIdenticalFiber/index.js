import request from '@/utils/request'


// 获取光路列表
export function queryRouterList(params) {
  return request({
    url: '/analysis/routerIdenticalFiber/queryRouter',
    method: 'get',
    params: params
  })
}

// 获取光路列表
export function queryRouterByCondition(params) {
  return request({
    url: '/analysis/routerIdenticalFiber/queryRouterByCondition',
    method: 'get',
    params: params
  })
}

// 根据id获取光路
export function queryRouterById(params) {
  return request({
    url: '/analysis/routerIdenticalFiber/queryRouterById',
    method: 'get',
    params: params
  })
}

// 不可靠路由分析
export function analysis(data) {
  return request({
    url: '/analysis/routerIdenticalFiber/analysis',
    method: 'post',
    data: data
  })
}


