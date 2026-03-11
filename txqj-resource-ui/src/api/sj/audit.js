import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/*查询数据源树*/
export function tree() {
  return request({
    url: '/model/treewithversionMiddle',
    method: 'get'
  })
}
/*获取资源树*/
export function customTree(data) {
  return request({
    url:'/show/listwithcolumninfoQuery',
    method: 'get',
    params:data
  })
}
//获取数据源列表
export function getDbList() {
  return request({
    url: '/dataaccess/db/list',
    method: 'get'
  })
}
//获取主键
export function getPrimaryKeyName(data) {
  return request({
    url: '/datatask/getPrimaryKeyName',
    method: 'post',
    params: data
  })
}

//获取中间库数据源表属性信息
export function getMiddleTableClu(data) {
  return request({
    url: '/resourceManage/initgriddatasite',
    method: 'get',
    params: data
  })
}
// 获取中间库数据源表数据信息
export function getMidTable(data) {
  return request({
    url: '/resourceManage/listMiddle',
    method: 'post',
    data: data
  })
}
//获取数据源表数据信息
export function getTable(data) {
  return request({
    url: '/metadatasearch/list',
    method: 'post',
    data: data
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
    url: '/metadatasearch/sync',
    method: 'post',
    data: data
  })
}
/*获取展示配置*/
export function tabData(data) {
  return request({
    url:'/show/listwithcolumninfo',
    method: 'get',
    params:data
  })
}
// 获取表格下拉框数据
export function tableDropDownBox(data) {
  return request({
    url: '/verrules/tableDropDownBox',
    method: 'get',
    params:data
  })
}
//动态获取查询条件与属性
export function initgriddatasite(data) {
  return request({
    url:'/resourceManage/initgriddatasite',
    method: 'post',
    data
  })
}