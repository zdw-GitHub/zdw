import request from '@/utils/request'


// 获取光缆列表
export function getFiberOptionsApi(params) {
  return request({
    url: '/resources/fiber/listmeta',
    method: 'get',
    params:params
  })
}
// 获取光路AZ端站点ID
export function getLightPathAZSite(params) {
  return request({
    url: '/resources/tcopticalroadB/getLightPathAZSite',
    method: 'get',
    params:params
  })
}

// 获取光缆下纤芯列表
export function getFiberLineOptionsApi(params) {
  return request({
    url: '/resources/fiberline/listZs',
    method: 'get',
    params:params
  })
}


// 获取光路路由
export function getLightPathRouterApi(params) {
  return request({
    url: '/resources/routerrfiber/quitecrouter',
    method: 'get',
    params:params
  })
}
