import request from '@/utils/request'
/*查询数据源树*/
export function getTypeList() {
    return request({
        method: 'get',
        url: '/tech/type'
      })
  }