import request from '@/utils/request'



// 获取设备列表
export function getEquipmentOptionsApi(params) {
    return request({
        url: '/analysis/nMinusOne/queryDevice',
        method: 'get',
        params: params
    })
}

// 分析
export function getAnalysisData(params) {
    return request({
        url: '/analysis/nMinusOne/analysis',
        method: 'get',
        params: params
    })
}

// 保存
export function saveAnalysis(data) {
    return request({
        url: '/analysis/nMinusOne/saveAnalysis',
        method: 'post',
        data: data
    })
}

// 导出
export function analysisExport(params) {
    return request({
        url: '/analysis/nMinusOne/analysisExport',
        method: 'get',
        params: params
    })
}
// 分析
export function manyFiberAnalysis(data) {
  return request({
    url: '/analysis/manyFiber/analysis',
    method: 'post',
    data: data
  })
}
