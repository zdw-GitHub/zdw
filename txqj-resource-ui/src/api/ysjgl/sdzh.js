import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/* 获取元模型的关联关系 */
export function relationtype(data) {
  return request({
    url: '/relation/relationtype',
    method: 'post',
    data: data
  })
}
/*获取条件组 */
export function groupList(data) {
  return request({
    url: '/transformationConditionGroup/list',
    method: 'get',
    params:data
  })
}
/* 获取sql关联*/
export function getSql(data) {
  return request({
    url: '/relation/sql',
    method: 'get',
    params: data
  })
}
/* 获取字段关联*/
export function getRetable(data) {
  return request({
    url: '/relation/reTableList',
    method: 'get',
    params: data
  })
}
/* 获取元模型字段 */
export function metaData(data) {
  return request({
    url: '/uid/metaUid/column',
    method: 'get',
    params: data
  })
}
/* 获取关联表字段*/
export function columnList(data) {
  return request({
    url: '/dataaccess/column/list',
    method: 'get',
    params: data
  })
}
/* 根据modelId查询元数据的信息 */
export function modelShow(data) {
  return request({
    url: '/model/show',
    method: 'get',
    params: data
  })
}

/* 新增条件组*/
export function addConditionGroup(data) {
  return request({
    url: '/transformationConditionGroup',
    method: 'post',
    data: data
  })
}

/* 新增条件*/
export function addCondition(data) {
  return request({
    url: '/transformationCondition',
    method: 'post',
    data: data
  })
}

/*删除条件组*/
export function delConditionGroup(id) {
  return request({
    url: '/transformationConditionGroup/' +id,
    method: 'delete'
  })
}

/*查找条件list*/
export function ConditionList(data) {
  return request({
    url: '/transformationCondition/list',
    method: 'get',
    params: data
  })
}
/*删除条件*/
export function delCondition(ids) {
  return request({
    url: '/transformationCondition/' +ids,
    method: 'delete'
  })
}
/*修改条件组*/
export function editConditionGroup(data) {
  return request({
    url: '/transformationConditionGroup',
    method: 'put',
    data: data
  })
}
/* 修改条件*/
export function editCondition(data) {
  return request({
    url: '/transformationCondition',
    method: 'put',
    data: data
  })
}

//同步表格数据
export function syncData(data) {
  return request({
    url: '/metadatasearch/sync',
    method: 'post',
    data: data
  })
}
/*测试sql*/
export function testSQL(data) {
  return request({
    url: '/transformationCondition/testSQL',
    method: 'get',
    params: data
  })
}
