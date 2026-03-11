import request from '@/utils/request'


// 新增光缆与一次线路关系
export function addSgTcdevFiberRLine(data) {
  return request({
    url: '/resources/SgTcdevFiberRLine/',
    method: 'post',
    data: data
  })
}

// 删除光缆与一次线路关系
export function delSgTcdevFiberRLine(data) {
  return request({
    url: '/resources/SgTcdevFiberRLine/',
    method: 'delete',
    data: data
  })
}
