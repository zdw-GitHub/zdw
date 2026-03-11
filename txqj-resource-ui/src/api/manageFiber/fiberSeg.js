import request from '@/utils/request'

// 提交新增
export function submit(data) {
  return request({
    url: '/resources/sgTcdevFibersegB',
    method: 'post',
    data: data
  })
}

// 提交修改
export function submitEdit(data) {
  return request({
    url: '/resources/sgTcdevFibersegB',
    method: 'put',
    data: data
  })
}

export function delDataApi(id) {
  return request({
    url: '/resources/sgTcdevFibersegB/removeAll/' + id,
    method: 'delete',
  })
}


// 获取表格下拉框数据（正式库）
export function getFiberOptions(params) {
  return request({
    url: '/resources/fiber/selectdatameta',
    method: 'get',
    params:params
  })
}

// 获取表格下拉框数据(全部)
export function getFiberOptionsAll(params) {
  return request({
    url: '/resources/fiber/selectdataAll',
    method: 'get',
    params:params
  })
}

// 查询已绑定列表
export function getData(data) {
  return request({
    url: '/resources/sgTcdevFibersegB/listmeta',
    method: 'get',
    params:data
  })
}
// 获取选择光缆表格
export function getSelFiber(params) {
  return request({
    url: '/resources/sgTcdevFibersegB/selectdatameta',
    method: 'get',
    params:params
  })
}

// 获取表格下拉框数据
export function getSelFiberAwait(params) {
  return request({
    url: '/resources/sgTcdevFibersegB/selectdataAll',
    method: 'get',
    params:params
  })
}
  // 绑定光缆段与光缆关系
  export function addFiberRFiberSeg(data) {
    return request({
      url: '/resources/fibersegrfiber/addAll',
      method: 'post',
       data
    })
}
// 解绑光缆段与光缆关系
export function delFiberRFiberSeg(data) {
  return request({
    url: '/resources/fibersegrfiber/delFiberRFiberSeg',
    method: 'post',
    data
  })
}

export function getFiberSegByFiberData(data) {
  return request({
    url: '/resources/sgTcdevFibersegB/listslave',
    method: 'get',
    params:data
  })
}
export function getSegIdsByFiberId(data) {
  return request({
    url: '/resources/sgTcdevFibersegB/getSegIdsByFiberId',
    method: 'get',
    params:data
  })
}


