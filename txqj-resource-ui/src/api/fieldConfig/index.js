import request from '@/utils/request'

// 查询类型下拉框
export function getCategoryOptionsApi (query) {
  return request({
    url: '/category/list',
    method: 'get',
    params: query
  })
}

// 查询表下拉框
export function getTableOptionsApi (query) {
  return request({
    url: '/model/list',
    method: 'get',
    params: query
  })
}

// 查询字段下拉框
export function getColumnOptionsApi (query) {
  return request({
    url: '/tech/list',
    method: 'get',
    params: query
  })
}

// 获取表格内容
export function getTableDataApi (params) {
  return request({
    url: '/rule/getModelRuleList',
    method: 'get',
    params: params
  })
}

// 新增/编辑规则
export function addAndEditCommitApi (params) {
  return request({
    url: '/rule/modifyMetaRule',
    method: 'post',
    data: params
  })
}

// 根据id获取内容
export function getInfoByIdApi (params) {
  return request({
    url: '/rule/getModelRuleInfo',
    method: 'get',
    params: params
  })
}

// 删除
export function delCommitApi (id) {
  return request({
    url: '/rule/deleteRules?ids=' + id,
    method: 'delete',
  })
}

// 查询日志
export function getLogTableDataApi (params) {
  return request({
    url: '/ruleLog/list',
    method: 'get',
    params: params
  })
}