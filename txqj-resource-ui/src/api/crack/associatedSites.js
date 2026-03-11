import request from '@/utils/request'

// 机框关联设备
export function cabinetAssociatedSites(data) {
  return request({
    url: '/resCommon/cabinetAssociatedSites',
    method: 'post',
    data:data
  })
}
