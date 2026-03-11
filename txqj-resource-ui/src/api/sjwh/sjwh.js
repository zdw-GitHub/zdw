import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/*获取modelid*/
export function getModeId(params) {
  return request({
    url:'/datatask/getModeId?taskNo='+params,
    method: 'post'
  })
}
/*查看数据信息*/
export function getInfo(params) {
  return request({
    url:'/datatask/getInfo',
    method: 'post',
    data:params
  })
}
/*更新数据信息*/
export function update(params) {
  return request({
    url:'/datatask/update',
    method: 'post',
    data:params
  })
}
/*删除数据信息*/
export function deltask(params) {
  return request({
    url:'/datatask/delete',
    method: 'post',
    data:params
  })
}
export function updateByTaskNo(params) {
  return request({
    url:'/datatask/updateByTaskNo'+params,
    method: 'post',
  })
}
/*获取数据信息*/
export function listMiddle(params) {
  return request({
    url:'/datatask/listMiddle',
    method: 'post',
    data:params
  })
}

