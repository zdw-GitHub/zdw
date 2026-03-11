import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/*查询表格数据*/
export function getList(query) {
  return request({
    url: '/result/CheckResult',
    method: 'get',
    params: query
  })
}
/*获取维护单位列表*/
export function selectCompany() {
  return request({
    url: '/result/selectCompany',
    method: 'get'
  })
}
/*获取问题类别列表*/
export function selectType() {
  return request({
    url: '/result/selectType',
    method: 'get'
  })
}
/*引导列表*/
export function guide(query) {
  return request({
    url: '/result/guide',
    method: 'get',
    params: query
  })
}
// 保存引导
export function saveSta(data) {
  return request({
    url:'/resourceManage',
    method:'put',
    data:data
  })
}
//删除
export function deleteRule(data) {
  return request({
    url: '/result/deleteRule'+data,
    method: 'post',
    data: data
  })
}
//？手动校验
export function handRules() {
  return request({
    url: '/assrules/assVerification',
    method: 'get'
  })
}
export function handRules1() {
  return request({
    url: '/verrules/manualVerification',
    method: 'get'
  })
}
//获取资源类型
export function selectResource(query) {
  return request({
    url:'/result/downDrop',
    method:'get',
    params: query
  })
}






