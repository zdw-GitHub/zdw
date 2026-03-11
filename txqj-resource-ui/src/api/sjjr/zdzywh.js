import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";
/*查询表格*/
export function getData(query) {
    return request({
      url: '/resourceManage/list',
      method: 'get',
      params: query
    })
  }
// 提交新增
export function submit(data) {
    return request({
      url: '/resourceManage',
      method: 'post',
      data
    })
  }

// 删除站点
export function deleteStation(ids) {
  return request({
    url: '/resourceManage/'+ ids,
    method: 'delete',
  })
}
  /*查询下拉调度等级*/
export function getDsp() {
    return request({
      url: '/resourceManage/devTypeOptions',
      method: 'get',
    })
}
/*查询下拉单位*/
  export function getCompany(query) {
    return request({
      url: '/resourceManage/companyOptions',
      method: 'get',
      params:query
    })
  }
/*查询下拉所属通信网*/
export function getGrid() {
  return request({
    url: '/resourceManage/gridOptions',
    method: 'get',
  })
}
export function getlevel() {
  return request({
    url: '/resourceManage/getSiteLevelOptions',
    method: 'get',
  })
}
/*查询是否必填*/
export function getGuide() {
  return request({
    url: '/resourceManage/addGuide',
    method: 'get',
  })
}
/*电压等级拉框*/
export function voltageOptions() {
  return request({
    url:'/resourceManage/getVoltageOptions',
    method: 'get'
  })
}
/*电压等级拉框*/
export function getTypeSite() {
  return request({
    url:'/resourceManage/getSiteTypeOptions',
    method: 'get'
  })
}
/*电压等级拉框*/
export function modelId() {
  return request({
    url:'/resourceManage/getModelId',
    method: 'get'
  })
}
/*检验传字段*/
export function getColunmId(query) {
  return request({
    url:'/resourceManage/getColunmId',
    method: 'get',
    params: query
  })
}
/*检验传填入值*/
export function getVerrules(query) {
  return request({
    url:'/verrules/singleAttributeVerification',
    method: 'get',
    params: query
  })
}
/*错误站点保存传值*/
export function errorStation(data) {
  return request({
    url:'/result/create',
    method: 'post',
    data: data
  })
}
//获取新增全部规则
export function selectAllRules() {
  return request({
    url: '/verrules/selectByModeIdVersionforCreate',
    method: 'get'
  })
}
//获取运行状态下拉
export function selectStatusList() {
  return request({
    url: '/resourceManage/getSiteStatusOptions',
    method: 'get'
  })
}
