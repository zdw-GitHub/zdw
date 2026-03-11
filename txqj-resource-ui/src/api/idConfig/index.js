import request from '@/utils/request'

// 查询类型下拉框
export function getCategoryNameOptionsApi () {
  return request({
    url: '/category',
    method: 'get',
    // params: query
  })
}

// 查询表下拉框
export function getModelNameOptionsApi (name) {
  return request({
    url: '/model/selectMetaModelWithoutIdRuleConfig/' + name,
    method: 'get',
    // params: query
  })
}

// 查询字段下拉框
export function getDataManagementOrgCodeAttributeNameOptionsApi (params) {
  return request({
    url: '/tech/listDownDrop',
    method: 'get',
    params: params
  })
}

// 获取表格内容
export function getTableDataApi (params) {
  return request({
    url: '/idRule/list',
    method: 'get',
    params: params
  })
}

// 根据id获取内容
export function getInfoByIdApi (id) {
  return request({
    url: '/idRule/' + id,
    method: 'get',
  })
}

// 新增提交
export function addCommitApi (data) {
  return request({
    url: '/idRule',
    method: 'post',
    data: data
  })
}

// 修改提交
export function editCommitApi (data) {
  return request({
    url: '/idRule',
    method: 'put',
    data: data
  })
}

// 删除
export function delCommitApi (id) {
  return request({
    url: '/idRule/' + id,
    method: 'delete',
  })
}

// 获取默认配置
export function getDefaultConfigApi (name) {
  return request({
    url: '/idRule/infoByModelName/' + name,
    method: 'get',
  })
}

// 更新默认配置
export function configDefaultCommitApi (data) {
  return request({
    url: '/idRule/addDefault',
    method: 'post',
    data: data
  })
}

// 获取日志表格内容
export function getLogTableDataApi (params) {
  return request({
    url: '/idConversionLog/list',
    method: 'get',
    params: params
  })
}

// 批量转换id
export function transitionIdApi (modelName) {
  return request({
    url: '/idRule/batchConversionId/' + modelName,
    method: 'get',
  })
}