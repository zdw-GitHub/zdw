import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 生成数据 数据校验 submitDataVerifyApi
export function submitDataVerifyApi(params) {
  return request({
    url: '/fiberedit/fiberedit-task/audit',
    method: 'get',
    params:params
  })
}
export function latestPdConversion(params) {
  return request({
    url: '/flowable/flowable/definition/latestPdConversion',
    method: 'get',
    params:params
  })
}

export function synchronousOpticalCableRelatedData(params) {
  return request({
    url: '/fiberedit/fiberedit-res/synchronousOpticalCableRelatedData',
    method: 'get',
    params:params
  })
}
