import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

//获取任务组
export function getGroup(id) {
  return request({
    url: '/schedule/group/' + parseStrEmpty(id),
    method: 'get'
  })
}
/*查询列表*/
export function listJob(data) {
  return request({
    url: '/schedule/job/list',
    method: 'get',
    params:data
  })
}
//添加任务
export function addJob(data) {
  return request({
    url: '/schedule/job',
    method: 'post',
    data: data
  })
}
//获取单个任务对象getSjyById
export function getById(id) {
  return request({
    url: '/schedule/job/' + parseStrEmpty(id),
    method: 'get'
  })
}
// 修改任务
  export function updateJob(data) {
  return request({
    url: '/schedule/job',
    method: 'put',
    data: data
  })
}
// 删除任务
export function delJob(userId) {
  return request({
    url: '/schedule/job/' + userId,
    method: 'delete'
  })
}
/*查询数据源tree*/
export function getDbTree() {
  return request({
    url: '/schedule/job/jobDbList',
    method: 'get'
  })
}
/*查询源模型tree*/
export function getTableTree(data) {
  return request({
    url: '/schedule/job/jobTableList',
    method: 'get',
    params:data
  })
}
/*查询任务日志列表*/
export function listLog(data) {
  return request({
    url: '/schedule/job/log/list',
    method: 'get',
    params:data
  })
}
//获取单个数据源对象
export function getDbById(userId) {
  return request({
    url: '/schedule/job/db/' + parseStrEmpty(userId),
    method: 'get'
  })
}
//获取单个数据源表格对象
export function getTableById(userId) {
  return request({
    url: '/schedule/job/table/' + parseStrEmpty(userId),
    method: 'get'
  })
}
//获取源模型同步条件
export function jobTableConditionList(data) {
  return request({
    url: '/schedule/job/jobTableConditionList',
    method: 'get',
    params:data
  })
}
