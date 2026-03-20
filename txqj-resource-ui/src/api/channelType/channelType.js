import request from '@/utils/request'


// 查询沟道列表
export function queryChannelType(query) {
    return request({
      url: '/sgTcconTcpipesegB/queryList',
      method: 'get',
      params: query
    })
  }
  // 根据查询已绑定光缆段
export function querysegByPipoId(pipoId) {
    return request({
      url: '/tcpipeseg/queryseg?pipoId='+pipoId,
      method: 'get',
    })
  }

    // 根据查询未绑定光缆段
export function querysegbysiteBySiteId(pipoId,segName,siteId) {
    return request({
      url: '/tcpipeseg/querysegbysite?segName='+segName+'&siteId='+siteId+'&pipoId='+pipoId,
      method: 'get',
    })
  }

    // 获取沟道基本信息
export function sgTcconTcpipesegBByPipoId(pipoId) {
    return request({
      url: '/sgTcconTcpipesegB/'+pipoId,
      method: 'get',
    })
  }

  /*站点下拉框*/
export function sgTcconTcsiteBOptions() {
    return request({
      url:'/sgTcconTcsiteB/selectList',
      method: 'get'
    })
  }

  // 新增
  export function addChannelType(data) {
    return request({
      url: '/sgTcconTcpipesegB',
      method: 'post',
      data: data
    })
  }

  // 修改
  export function updateChannelType(data) {
    return request({
      url: '/sgTcconTcpipesegB',
      method: 'put',
      data: data
    })
  }

  // 删除沟道
  export function delChannelType(id) {
    return request({
      url: '/sgTcconTcpipesegB/' + id,
      method: 'delete'
    })
  }

  // 删除光缆段和沟道的关联关系
  export function delTcpipeseg(id) {
    return request({
      url: '/tcpipeseg/' + id,
      method: 'delete'
    })
  }

  // 新增光缆段和沟道
  export function addTcpipesegAndChannel(data) {
    return request({
      url: '/tcpipeseg',
      method: 'post',
      data: data
    })
  }

  // 导出
  export function exportChannelType(query) {
    return request({
      url: '/sgTcconTcpipesegB/export',
      method: 'get',
      params: query
    })
  }


