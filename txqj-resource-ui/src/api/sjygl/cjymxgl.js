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
//获取单个数据源表格对象
export function getTableById(userId) {
  return request({
    url: '/dataaccess/table/' + parseStrEmpty(userId),
    method: 'get'
  })
}
// 修改数据源表格
export function updateTable(data) {
  return request({
    url: '/dataaccess/table',
    method: 'put',
    data: data
  })
}
// 删除数据源表格
    export function delTable(userId) {
  return request({
    url: '/dataaccess/table/' + userId,
    method: 'delete'
  })
}
//获取数据源表属性信息
export function getDbTableClu(query) {
  return request({
    url: '/dataaccess/column/list',
    method: 'get',
    params: query
  })
}
/*通过id获取表格属性*/
export function getColumnById(userId) {
  return request({
    url: '/dataaccess/column/' + parseStrEmpty(userId),
    method: 'get'
  })
}
// 修改数据源表格属性
export function updateClu(data) {
  return request({
    url: '/dataaccess/column',
    method: 'put',
    data: data
  })
}
