import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/*查询数据源树*/
export function sjyTree() {
  return request({
    url: '/dataaccess/db/tree',
    method: 'get'
  })
}
//获取数据源列表
export function getDbList() {
  return request({
    url: '/dataaccess/db/list',
    method: 'get'
  })
}
//获取单个数据源对象
export function getDbById(userId) {
  return request({
    url: '/dataaccess/db/' + parseStrEmpty(userId),
    method: 'get'
  })
}
//获取单个数据源表格对象
export function getTableById(userId) {
  return request({
    url: '/dataaccess/table/' + parseStrEmpty(userId),
    method: 'get'
  })
}

//获取数据源表属性信息
export function getTableClu(query) {
  return request({
    url: '/dataaccess/column/list',
    method: 'get',
    params: query
  })
}
//获取数据源表数据信息
export function getTable(query) {
  return request({
    url: '/dataaccess/common/list',
    method: 'get',
    params: query
  })
}
// 修改数据源关联条件
export function updateTable(data) {
  return request({
    url: '/dataaccess/table',
    method: 'put',
    data: data
  })
}
//同步表格数据

export function tbData(data) {
  return request({
    url: '/dataaccess/datatransfer/datasyncwithcondition',
    method: 'get',
    params: data
  })
}
/*表格批量同步操作*/
export function batchDatasyncwithcondition(data) {
  return request({
    url: '/dataaccess/datatransfer/batchDatasyncwithcondition',
    method: 'post',
    data: data
  })
}


/** 同步条件设置*/
//获取同步条件列表
export function getConditionByTableId(data) {
  return request({
    url: '/dataaccess/condition/getConditionByTableId',
    method: 'get',
    params:data
  })
}
//新增同步条件
export function conditionadd(data) {
  return request({
    url: '/dataaccess/condition',
    method: 'post',
    data:data
  })
}
//修改同步条件
export function conditionedit(data) {
  return request({
    url: '/dataaccess/condition',
    method: 'put',
    data:data
  })
}
// 删除同步条件
export function delcondition(id) {
  return request({
    url: '/dataaccess/condition/' + id,
    method: 'delete'
  })
}
//搜索同步条件
export function searchcondition(params) {
  return request({
    url: '/dataaccess/condition/list',
    method: 'get',
    params
  })
}
