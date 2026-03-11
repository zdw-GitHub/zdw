import request from '@/utils/request'
/*查询数据源树*/
export function sqlEdit(data) {
  return request({
    url: '/relation/sqlEdit',
    method: 'put',
    data: data
  })
}

/*唯一标识组成界面的查询方法*/
export function disposeMetaDataUid(data) {
  return request({
    url: '/uid/disposeMetaDataUid',
    method: 'get',
    params: data
  })
}

/*根据元模型id查询该元数据表字段信息*/
export function metaUid(data) {
  return request({
    url: '/uid/metaUid/column',
    method: 'get',
    params: data
  })
}

/*根据采集源模型id查询该采集源数据表字段信息*/
export function sourceUid(data) {
  return request({
    url: '/uid/sourceUid/column',
    method: 'get',
    params: data
  })
}
/*获取采集源模型的下拉框*/
export function sourcelist(data) {
  return request({
    url: '/dataaccess/column/list',
    method: 'get',
    params: data
  })
}

/*配置保存元模型 或 源数据模型的唯一标识信息*/
export function modifyMetaUid(data) {
  return request({
    url: '/uid/modifyMetaUid',
    method: 'post',
    data: data
  })
}
/*配置保存元模型以及元模型映射源数据模型唯一标识信息*/
export function modify(data) {
  return request({
    url: '/uid/modify',
    method: 'post',
    data: data
  })
}
