import request from '@/utils/request'

// 执行一次分析
export function analyseApi(data) {
  return request({
    url: '/analysis/lineAnalysis/analyzeCommonFiberAndThreeRoutByOneLineId',
    method: 'get',
    params:data
  })
}
// 获取上次分析时间
export function getAnalyseTimeApi(data) {
  return request({
    url: '/analysis/lineAnalysis/getAnalyTimeByLineId',
    method: 'get',
    params:data
  })
}
// 查看业务 高亮业务路由(根据业务 查路由)
export function getBuzRouterApi(data) {
  return request({
    url: '/analysis/lineAnalysis/getBuzRoutByLineId',
    method: 'get',
    params:data
  })
}
// 获取同缆光缆表格
export function getCommonFiberApi(data) {
  return request({
    url: '/analysis/lineAnalysis/getCommonFiberByLineId',
    method: 'get',
    params:data
  })
}
// 获取地图点
export function getSiteApi(data) {
  return request({
    url: '/analysis/lineAnalysis/getSiteByLineId',
    method: 'get',
    params:data
  })
}
// 获取地图线
export function getFiberApi(data) {
  return request({
    url: '/analysis/lineAnalysis/getFiberByLineId',
    method: 'get',
    params:data
  })
}

// 获取三路由
export function getThreeRoutApi(data) {
  return request({
    url: '/analysis/lineAnalysis/getBuzSiteRoutByLineId',
    method: 'get',
    params:data
  })
}
// 获取光缆承载业务
export function getBuzByFiber (data) {
  return request({
    url: '/analysis/lineAnalysis/getBuzBuyFiberId',
    method: 'get',
    params:data
  })
}
