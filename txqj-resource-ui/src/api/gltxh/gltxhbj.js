import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";
/* 提交选择已存在光缆 */
export function chooseLink(data) {
  return request({
    url: '/fiberedit/fiberedit-fiber/showChangeFibersTopo',
    method: 'get',
    params: data
  })
}
/* 查询光缆数据 */
export function selectLink(data) {
    return request({
      url: '/fiberedit/fiberedit-fiber/allFiberList',
      method: 'get',
      params:data
    })
}
/*查询站点数据*/
export function selectSite() {
  return request({
    url: '/fiberedit/publicclass/selectSite',
    method: 'get',
  })
}
/*查询光缆段纤芯数量*/
export function selectLineNum() {
  return request({
    url: '/fiberedit/publicclass/selectSegLineNum',
    method: 'get',
  })
}
/*资源下拉框信息获取*/
export function getParaValueByresType(params) {
  return request({
    url: '/fiberedit/publicclass/getParaValueByresType',
    method: 'get',
    params:params
  })
}
/*根据下拉框信息，获取下拉框*/
export function tableDropDownBox(params) {
  return request({
    url: '/verrules/tableDropDownBox',
    method: 'get',
    params:params
  })
}

/*新增‘资源展示’的站点数据*/
export function addSite(data) {
  return request({
    url: '/fiberedit/fiberedit-res',
    method: 'post',
    data:data
  })
}
/*删除资源展示中的站点*/
export function delSite(id) {
  return request({
    url: '/fiberedit/fiberedit-res/{taskShowId}?taskShowId='+id,
    method: 'delete',
  })
}

/*查询‘资源展示’数据*/
export function list(params) {
  return request({
    url: '/fiberedit/fiberedit-res/list',
    method: 'get',
    params:params
  })
}
//接头盒
/*新增接头盒*/
export function addTcon(params) {
  return request({
    url: '/fiberedit/fiberedit-tcon',
    method: 'post',
    data:params
  })
}
/*修改接头盒属性*/
export function xgTcon(params) {
  return request({
    url: '/fiberedit/fiberedit-tcon',
    method: 'put',
    data:params
  })
}
/*删除接头盒*/
// export function delTcon(data) {
//   return request({
//     url: '/fiberedit/fiberedit-tcon/{id}',
//     method: 'delete',
//     data:data
//   })
// }
// http://localhost:82/dev-api/fiberedit%E2%80%8B/fiberedit-tcon%E2%80%8B/f40d45f7dc8240478
// http://localhost:82/dev-api/fiberedit/fiberedit-res/%7BtaskShowId%7D?taskShowId=f40d45f7dc8240478
// 删除资源展示中的接头盒
export function delTcon(data) {
  return request({
    url: '/fiberedit/fiberedit-tcon/{id}',
    method: 'delete',
    data:data
  })
}


// /*删除光缆段*/
// export function delSeg(data) {
//   return request({
//     url: '/fiberedit/fiberedit-fiberseg/{ids}',
//     method: 'delete',
//     data:data
//   })
// }

//光缆段
/*新增光缆段*/
export function addSeg(params) {
  return request({
    url: '/fiberedit/fiberedit-fiberseg',
    method: 'post',
    data:params
  })
}
/*修改光缆段属性*/
export function xgSeg(params) {
  return request({
    url: '/fiberedit/fiberedit-fiberseg',
    method: 'put',
    data:params
  })
}
/*删除光缆段*/
export function delSeg(data) {
  return request({
    url: '/fiberedit/fiberedit-fiberseg/{ids}',
    method: 'delete',
    data:data
  })
}

/*删除数据光缆段*/
export function delSegData(data) {
  return request({
    url: '/fiberedit/fiberedit-fiberseg/removeExist',
    method: 'delete',
    data:data
  })
}

//保存更新站点数据
export function editSite(params) {
  return request({
    url: '/fiberedit/fiberedit-res',
    method: 'put',
    data:params
  })
}
//保存时更新接头盒数据
export function editTcon(params) {
  return request({
    url: '/fiberedit/fiberedit-tcon',
    method: 'put',
    data:params
  })
}
//保存时更新光缆段数据
export function editSeg(params) {
  return request({
    url: '/fiberedit/fiberedit-fiberseg',
    method: 'put',
    data:params
  })
}

/*光缆*/
//删除光缆
export function delFiber(data) {
  return request({
    url: '/fiberedit/fiberedit-fiber/{ids}',
    method: 'delete',
    data:data
  })
}

//删除光缆数据
export function delFiberData(data) {
  return request({
    url: '/fiberedit/fiberedit-fiber/removeExist',
    method: 'delete',
    data:data
  })
}

/*修改光缆属性*/
export function xgFiber(params) {
  return request({
    url: '/fiberedit/fiberedit-fiber',
    method: 'put',
    data:params
  })
}

/*校验*/
//获取校验参数
export function queryParaValueByRes(params) {
  return request({
    url: '/fiberedit/publicclass/queryParaValueByRes',
    method: 'get',
    params:params
  })
}
//属性校验
export function singleAttributeVerification(params) {
  return request({
    url: '/verrules/singleAttributeVerification',
    method: 'get',
    params:params
  })
}
/*生成数据*/
//生成数据时，获取校验数据
export function getCheckDate(params) {
  return request({
    url: '/fiberedit/fiberedit-res/getCheckDate',
    method: 'get',
    params:params
  })
}
/*生成光缆*/
export function saveFiber(params) {
  return request({
    url:'/fiberedit/fiberedit-fiber/saveFiber',
    method:'get',
    params:params
  })
}
// 获取表名
export function getTaskName(params) {
  return request({
    url: '/fiberedit/publicclass/queryTableNameByResType',
    method: 'get',
    params:params
  })
}

/*获取光缆横截面*/
export function getFiberCoreDetailApi (params) {
  return request({
    url:'/fiberedit/fiberedit-fiber/getFiberLinesByFiberId',
    method:'get',
    params:params
  })
}

//更新坐标
export function updateCoordinates(params) {
  return request({
    url: '/fiberedit/fiberedit-res/updateCoordinates',
    method: 'post',
    data:params
  })
}
