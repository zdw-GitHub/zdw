import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/*查询数据源树*/
export function getTreeData() {
  return request({
    url: '/model/tree',
    method: 'get'
  })
}
export function AddTableData() {
  return request({
    url: '/model',
    method: 'post'
  })
}
export function selectcategoryNameId() {
  return request({
    url: '/category/selectNameId',
    method: 'get'
  })
}
export function getversions() {
  return request({
    url: '/model/versions?modelId=' + this.modelId,
    method: 'get'
  })
}
export function selectNameId() {
  return request({
    url: '/label/selectNameId',
    method: 'get'
  })
}
