import request from '@/utils/request'



// 获取线路列表
export function queryLineList(params) {
    return request({
        url: '/analysis/unreliableRouting/queryLineList',
        method: 'get',
        params: params
    })
}
// 不可靠路由分析
export function analysis(params) {
    return request({
        url: '/analysis/unreliableRouting/analysis',
        method: 'get',
        params: params
    })
}


