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
export function getForm(id) {
  return request({
    url: '/resourceManage/'+id,
    method: 'get'
  })
}
export function selectRule(query) {
  return request({
    url:'/verrules/selectByModeIdVersion',
    method:'get',
    params: query
  })
}
// 提交修改
export function submitEdit(data) {
  return request({
    url: '/resourceManage',
    method: 'put',
    data: data
  })
}
/*错误站点保存传值*/
export function errorStation(data) {
  return request({
    url:'/result/createToUpdateRule',
    method: 'post',
    data: data
  })
}
/*错误站点保存传值*/
export function errorEditStation(data) {
  return request({
    url:'/result/createToUpdateRule',
    method: 'post',
    data: data
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
