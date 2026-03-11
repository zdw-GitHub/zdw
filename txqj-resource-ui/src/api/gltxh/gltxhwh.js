import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/*查询光缆编制任务列表*/
export function getList(params) {
  return request({
    url: '/fiberedit/fiberedit-task/list',
    method: 'get',
    params:params
  })
}
/*新增光缆编制任务列表*/
export function addTask(data) {
  return request({
    url: '/fiberedit/fiberedit-task',
    method: 'post',
    data:data
  })
}
/*删除光缆编制任务*/
export function delTask(id) {
  return request({
    url: '/fiberedit/fiberedit-task/'+id,
    method: 'delete',
  })
}
/*获取一条光缆编制任务*/
export function getOneTask(id) {
  return request({
    url: '/fiberedit/fiberedit-task/'+id,
    method: 'get'
  })
}

//保存更新编制任务列表数据
export function updataTaskApi(params) {
  return request({
    url: '/fiberedit/fiberedit-task',
    method: 'put',
    data:params
  })
}