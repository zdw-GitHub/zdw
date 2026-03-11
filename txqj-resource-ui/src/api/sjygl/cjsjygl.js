import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/*查询数据源树*/
export function sjyTree() {
  return request({
    url: '/dataaccess/db/tree',
    method: 'get'
  })
}

//添加数据源
export function addSjy(data) {
  return request({
    url: '/dataaccess/db',
    method: 'post',
    data: data
  })
}
//获取单个数据源对象getSjyById
export function getSjyById(userId) {
  return request({
    url: '/dataaccess/db/' + parseStrEmpty(userId),
    method: 'get'
  })
}
// 修改数据源
export function updateSjy(data) {
  return request({
    url: '/dataaccess/db',
    method: 'put',
    data: data
  })
}
// 删除数据源
export function delSjy(userId) {
  return request({
    url: '/dataaccess/db/' + userId,
    method: 'delete'
  })
}
//获取数据源表信息
export function getDbTable(query) {
  return request({
    url: '/dataaccess/datatransfer/tables',
    method: 'get',
    params: query
  })
}
/*查看表属性*/
export function getTableClu(query) {
  return request({
    url: '/dataaccess/datatransfer/columns',
    method: 'get',
    params: query
  })
}
/*表格同步操作*/
export function tbTableClu(query) {
  return request({
    url: '/dataaccess/datatransfer/transfer',
    method: 'get',
    params: query
  })
}

/*表格批量同步操作*/
export function batchTransfer(data) {
  return request({
    url: '/dataaccess/datatransfer/batchTransfer',
    method: 'post',
    data: data
  })
}
