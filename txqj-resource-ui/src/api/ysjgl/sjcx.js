import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/*查询数据源树*/
export function sjyTree() {
  return request({
    url: '/model/treewithversion',
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

//获取数据源表属性信息
export function getTableClu(data) {
  return request({
    url: '/metadatasearch/initgriddata',
    method: 'post',
    data: data
  })
}
//获取中间库数据源表属性信息
export function getMiddleTableClu(data) {
  return request({
    url: '/site/b/initgriddatasite',
    method: 'post',
    data
  })
}
// 获取中间库数据源表数据信息
export function getMidTable(data) {
  return request({
    url: '/metadatasearch/listMiddle',
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

/*获取展示配置*/
export function editCommit(data) {
  return request({
    url:'/show',
    method: 'put',
    data:data
  })
}

//获取单个站点对象
export function getForm(data) {
  return request({
    url: '/site/b/getInfo',
    method: 'post',
    data:data
  })
}

/*获取展示基本信息*/
export function getInfo(id) {
  return request({
    url:'/model/' + id,
    method: 'get',
  })
}
/*获取展示基本信息*/
export function getInfo1(id) {
  return request({
    url:'/category/' + id,
    method: 'get',
  })
}