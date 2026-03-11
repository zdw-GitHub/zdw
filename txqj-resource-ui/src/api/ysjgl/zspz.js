import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询菜单列表
export function listMenu(query) {
  return request({
    url: '/menu/sysMenuList',
    method: 'get',
    params: query
  })
}
/*查询数据源树*/
export function sjyTree(data) {
  return request({
    url: '/model/treeByTableName',
    method: 'get',
    params:data
  })
}
/*查询属性列表*/
export function column(data) {
  return request({
    url: '/uid/metaUid/column',
    method: 'get',
    params:data
  })
}
/*修改配置*/
export function editShow(data) {
  return request({
    url: '/show',
    method: 'put',
    data:data
  })
}
