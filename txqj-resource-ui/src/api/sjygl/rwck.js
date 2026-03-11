import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/*查询数据源树*/
export function sjyTree(data) {
  return request({
    url: '/schedule/job/tree',
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
//获取单个任务对象getById
export function getById(id) {
  return request({
    url: '/schedule/job/' + parseStrEmpty(id),
    method: 'get'
  })
}
