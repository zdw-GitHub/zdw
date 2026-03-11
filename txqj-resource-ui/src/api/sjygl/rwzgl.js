import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";


// 查询任务组列表
export function listGroupJob(query) {
    return request({
      url: '/schedule/group/list',
      method: 'get',
      params: query
    })
  }
// 任务组状态修改
export function changeStatus(jobGroupId, status) {
    const data = {
      jobGroupId,
      status
    }
    return request({
      url: '/schedule/group/changeStatus',
      method: 'put',
      data: data
    })
  }
//获取单个任务组对象
export function getGroupInfo(id) {
  return request({
    url: '/schedule/group/' + parseStrEmpty(id),
    method: 'get'
  })
}
// 定时任务立即执行一次
export function runJobGroup(jobGroupId) {
    const data = {
      jobGroupId
    }
    return request({
      url: '/schedule/group/run',
      method: 'get',
      data: data
    })
  }
// 新增定时任务调度
export function addJob(data) {
    return request({
      url: '/schedule/group',
      method: 'post',
      data: data
    })
  }
// 删除定时任务调度
export function delJobGroup(jobGroupIds) {
    return request({
      url: '/schedule/group/' + jobGroupIds,
      method: 'delete'
    })
  } 
  // 修改定时任务调度
  export function updateJob(data) {
    return request({
      url: '/schedule/group',
      method: 'put',
      data: data
    })
  }
// 查询最近一次任务组日志
export function getRecentGroup(query) {
    return request({
      url: '/schedule/group/GroupLogListLast',
      method: 'get',
      params: query
    })
  }
// 查询任务组日志历史
export function getHistoryGroup(query) {
    return request({
      url: '/schedule/group/GroupLogListHis',
      method: 'get',
      params: query
    })
  }
// 查询异常信息日志
export function getErrorLogList(query) {
  return request({
    url: '/schedule/error/list',
    method: 'get',
    params: query
  })
}
// 查看任务组历史获取资源树任务组
export function getGroupId(jobGroupId) {
    return request({
      url: '/schedule/group/'+ jobGroupId,
      method: 'get'
    })
  }
// 查看任务组历史获取资源树下拉任务
export function getJobList(query) {
    return request({
      url: '/schedule/job/listTree',
      method: 'get',
      params: query
    })
  }